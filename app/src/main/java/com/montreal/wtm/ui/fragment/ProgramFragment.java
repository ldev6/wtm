package com.montreal.wtm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import com.montreal.wtm.utils.ui.fragment.BaseFragment;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public class ProgramFragment extends BaseFragment {

    private static final String TAG = ProgramFragment.class.getSimpleName();

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

        FirebaseData.INSTANCE.getMyShedule(getActivity(), requestListenerMySchedule);
        FirebaseData.INSTANCE.getSessions(getActivity(), requestListenerSession);
        FirebaseData.INSTANCE.getMyRatings(getActivity(), requestListenerRatings);

        setMessageViewInterface(this);
        showProgressBar();
        return v;
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
                                int i = 0;
                                for (int sessionID : sessionIds) {
                                    Session session = sessionHashMap.get("" + sessionID);

                                    String time = timeslot.getTime();
                                    if (sessionIds.size() > 1) {
                                        time = getStartDate(day.date, timeslot.startTime, timeslot.endTime,
                                            sessionIds.size(), i);
                                    }
                                    talks.add(new Talk(session, time, tracks.get(session.getRoomId()).title,
                                        session.isIncludedIn(savedSessions)));
                                    i++;
                                }
                            }
                        }

                        day.setTalks(talks);
                    }
                    return days;
                }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(days1 -> {

                    adapter = new ProgramFragmentPagerAdapter(getChildFragmentManager(), days1);
                    viewPager.setAdapter(adapter);
                    hideMessageView();
                });
            }

            @Override
            public void onCancelled(FirebaseData.ErrorFirebase errorType) {
                //TODO
                //String message = errorType == FirebaseData.INSTANCE.ErrorFirebase.network ? getString(R.string
                // .default_error_message) : getString(R.string.error_message_serveur_prob);
                //setMessageError(message);
            }
        };

    private FirebaseData.RequestListener<HashMap<String, Boolean>> requestListenerMySchedule =
        new FirebaseData.RequestListener<HashMap<String, Boolean>>() {
            @Override
            public void onDataChange(HashMap<String, Boolean> mySchedule) {
                Log.v("Saved schedule", " Saved schedule=" + mySchedule);
                savedSessions = mySchedule;
                hideMessageView();
                if (adapter != null) {
                    adapter.getItem(viewPager.getCurrentItem()).setSavedSession(mySchedule);
                }
                //hideMessageView();
            }

            @Override
            public void onCancelled(FirebaseData.ErrorFirebase errorType) {
                //TODO
                //String message = errorType == FirebaseData.INSTANCE.ErrorFirebase.network ? getString(R.string
                // .default_error_message) : getString(R.string.error_message_serveur_prob);
                //setMessageError(message);
            }
        };

    //function getEndTime(String date, startTime, endTime, totalNumber, number) {
    //    var timeStart = new Date(date + ' ' + startTime).getTime(), timeEnd = new Date(date + ' ' + endTime)
    // .getTime(),
    //        difference = Math.floor((timeEnd - timeStart) / totalNumber), result =
    //        new Date(timeStart + difference * number);
    //    var minutes = result.getMinutes();
    //    minutes = minutes > 9 ? minutes : '0' + minutes;
    //    return result.getHours() + ':' + minutes;
    //}

    public String getStartDate(String stringDate, String startTime, String endTime, int totalNumber, int number) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CANADA);
        try {
            String stringDateStart = stringDate + " " + startTime;
            String stringDateEnd = stringDate + " " + endTime;
            Date dateStart = format.parse(stringDateStart);
            Date dateEnd = format.parse(stringDateEnd);
            long timeStart = dateStart.getTime();
            long timeEnd = dateEnd.getTime();
            Log.v("Date", "Date = " + dateStart);

            double difference = Math.floor((timeEnd - timeStart) / totalNumber);
            Log.v("Date", "difference = " + difference);

            long newDateTime = (long) (timeStart + difference * number);

            Date result = new Date(newDateTime);
            int minutes = result.getMinutes();
            minutes = minutes > 9 ? minutes : '0' + minutes;
            Log.v("result", " result = " + result.getHours() + ":" + minutes);
            return result.getHours() + ":" + minutes;
        } catch (ParseException e) {
            Log.e(TAG, "ParseException", e);
        }
        return startTime + " " + endTime;
    }

    @Override
    public void retryOnProblem() {
        if (!isAdded()) {
            return;
        }
        FirebaseData.INSTANCE.getSessions(getActivity(), requestListenerSession);

        FirebaseData.INSTANCE.getMyShedule(getActivity(), requestListenerMySchedule);
    }

    private FirebaseData.RequestListener<HashMap<String, Long>> requestListenerRatings =
        new FirebaseData.RequestListener<HashMap<String, Long>>() {
            @Override
            public void onDataChange(@org.jetbrains.annotations.Nullable HashMap<String, Long> data) {
                Log.d(TAG, "Received ratings:" + data);
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
                FirebaseData.INSTANCE.getSchedule(getActivity(), requestListenerDays);
            }

            @Override
            public void onCancelled(@NotNull FirebaseData.ErrorFirebase errorType) {

            }
        };
}
