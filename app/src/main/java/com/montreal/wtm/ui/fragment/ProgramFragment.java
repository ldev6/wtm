package com.montreal.wtm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Day;
import com.montreal.wtm.model.Session;
import com.montreal.wtm.model.Talk;
import com.montreal.wtm.model.Timeslot;
import com.montreal.wtm.model.Track;
import com.montreal.wtm.ui.adapter.ProgramFragmentPagerAdapter;
import com.montreal.wtm.utils.ui.activity.BaseActivity;
import com.montreal.wtm.utils.ui.fragment.BaseFragment;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import timber.log.Timber;

public class ProgramFragment extends BaseFragment {

    private Disposable disposable;

    public static ProgramFragment newInstance() {
        ProgramFragment fragment = new ProgramFragment();
        return fragment;
    }

    private ViewPager viewPager;
    private HashMap<String, Session> sessionHashMap;
    private HashMap<String, Boolean> savedSessions;
    private ProgramFragmentPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.program_fragment, container, false);
        viewPager = v.findViewById(R.id.viewpager);
        TabLayout tabLayout = v.findViewById(R.id.sliding_tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        adapter = new ProgramFragmentPagerAdapter(getChildFragmentManager());

            FirebaseData.INSTANCE.getMyShedule(getActivity(), requestListenerMySchedule);
        FirebaseData.INSTANCE.getSessions(getActivity(), requestListenerSession);
        FirebaseData.INSTANCE.getMyRatings(getActivity(), requestListenerRatings);

        setMessageViewInterface(this);
        showProgressBar();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        disposable = ((BaseActivity) getActivity()).getLoginChanged().subscribe(this::onLoginChanged);
    }

    private void onLoginChanged(Boolean loggedIn) {
        Timber.d("Received login change on ProgramFragment %s", loggedIn);
        if (loggedIn != null && loggedIn) {
            retryOnProblem();
        }
    }

    private FirebaseData.RequestListener<ArrayList<Day>> requestListenerDays =
        new FirebaseData.RequestListener<ArrayList<Day>>() {
            @Override
            public void onDataChange(ArrayList<Day> days) {
                Single.fromCallable(() -> {
                    for (Day day : days) {
                        ArrayList<Track> tracks = day.getTracks();
                        ArrayList<Talk> talks = new ArrayList<>();
                        for (Timeslot timeslot : day.getTimeslots()) {
                            for (ArrayList<Integer> sessionIds : timeslot.sessionsId) {
                                if (sessionIds.size() > 1) {
                                    for (int pos = 0; pos < sessionIds.size(); pos++) {
                                        Session session = sessionHashMap.get(String.valueOf(sessionIds.get(pos)));
                                        String time = getTimePeriod(day.date, timeslot.startTime, timeslot.endTime,
                                            sessionIds.size(), pos);
                                        talks.add(new Talk(session, time, tracks.get(session.getRoomId()).title,
                                            session.isIncludedIn(savedSessions)));
                                    }
                                } else if (sessionIds.size() == 1) {
                                    Session session = sessionHashMap.get(String.valueOf(sessionIds.get(0)));
                                    String time = timeslot.getTime();
                                    talks.add(new Talk(session, time, tracks.get(session.getRoomId()).title,
                                        session.isIncludedIn(savedSessions)));
                                }
                            }
                        }

                        day.setTalks(talks);
                    }
                    return days;
                }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(days1 -> {

                    if(adapter != null) {
                        adapter.updateDays(days1);
                    } else {
                        adapter = new ProgramFragmentPagerAdapter(getChildFragmentManager(), days1);
                    }
                    viewPager.setAdapter(adapter);
                    hideMessageView();
                });
            }

            @Override
            public void onCancelled(FirebaseData.ErrorFirebase errorType) {
            }
        };

    private FirebaseData.RequestListener<HashMap<String, Boolean>> requestListenerMySchedule =
        new FirebaseData.RequestListener<HashMap<String, Boolean>>() {
            @Override
            public void onDataChange(HashMap<String, Boolean> mySchedule) {
                Timber.v(" Saved schedule " + mySchedule);
                savedSessions = mySchedule;
                hideMessageView();
                if (adapter != null) {
                    adapter.loadSavedSessions(mySchedule);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseData.ErrorFirebase errorType) {

            }
        };

    public String getTimePeriod(String stringDate, String startTime, String endTime, int totalNumber, int number) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CANADA);
        try {
            String stringDateStart = stringDate + " " + startTime;
            String stringDateEnd = stringDate + " " + endTime;
            Date dateStart = format.parse(stringDateStart);
            Date dateEnd = format.parse(stringDateEnd);
            long timeStart = dateStart.getTime();
            long timeEnd = dateEnd.getTime();

            long difference = (timeEnd - timeStart) / totalNumber;

            long newStartTime = timeStart + difference * number;
            long newEndTime = timeStart + difference * (number + 1);

            return getTime(newStartTime) + " - " + getTime(newEndTime);
        } catch (ParseException e) {
            Timber.e(e, "ParseException");
        }
        return startTime + " " + endTime;
    }

    protected String getTime(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        StringBuilder builder = new StringBuilder();
        builder.append(calendar.get(Calendar.HOUR_OF_DAY));
        builder.append(":");
        int minutes = calendar.get(Calendar.MINUTE);
        if (minutes <= 9) {
            builder.append("0");
        }
        builder.append(minutes);
        return builder.toString();
    }

    @Override
    public void retryOnProblem() {
        if (!isAdded()) {
            return;
        }

        FirebaseData.INSTANCE.getMyShedule(getActivity(), requestListenerMySchedule);
        FirebaseData.INSTANCE.getSessions(getActivity(), requestListenerSession);
        FirebaseData.INSTANCE.getMyRatings(getActivity(), requestListenerRatings);
    }

    private FirebaseData.RequestListener<HashMap<String, Long>> requestListenerRatings =
        new FirebaseData.RequestListener<HashMap<String, Long>>() {
            @Override
            public void onDataChange(@org.jetbrains.annotations.Nullable HashMap<String, Long> data) {
                Timber.d("Received ratings:" + data);
            }

            @Override
            public void onCancelled(@NotNull FirebaseData.ErrorFirebase errorType) {

            }
        };

    private FirebaseData.RequestListener<HashMap<String, Session>> requestListenerSession =
        new FirebaseData.RequestListener<HashMap<String, Session>>() {
            @Override
            public void onDataChange(HashMap<String, Session> sessionMap) {
                sessionHashMap = sessionMap;
                if(getActivity() != null) {
                    FirebaseData.INSTANCE.getSchedule(getActivity(), requestListenerDays);
                }
            }

            @Override
            public void onCancelled(@NotNull FirebaseData.ErrorFirebase errorType) {

            }
        };
}
