package com.royalenfield.reprime.ui.calendar;

import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.ui.motorcyclehealthalert.listeners.OnCalendarDateSelectedListener;
import com.royalenfield.reprime.ui.motorcyclehealthalert.utils.HealthAlertUtils;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.Calendar;
import java.util.List;

public class CalendarDialogFragment extends DialogFragment implements OnDateSelectedListener, View.OnClickListener, OnRangeSelectedListener {

    public static final String KEY_START_DATE = "keyStartDate";
    public static final String KEY_END_DATE = "KeyEndDate";
    private MaterialCalendarView widget;
    private TextView startMonth, startDate, startYear, endMonth, endDate, endYear;
    private OnCalendarDateSelectedListener onCalendarDateSelectedListener;
    private CalendarDay startSelectedDate;
    private List<CalendarDay> mDates;
    private int currentMonthIndex;
    private int currentYearIndex;
    private CalendarDay endSelectedDate;
    private CalendarDay lastDay;


    public void setCalendarDateSelectedListener(OnCalendarDateSelectedListener listener) {
        onCalendarDateSelectedListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CalendarDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.custom_calendar_view, container, false);
        initViews(dialogView);
        return dialogView;
    }

    private void initViews(View dialogView) {
        // Do all the stuff to initialize your custom view
        widget = dialogView.findViewById(R.id.calendarView);
        startMonth = dialogView.findViewById(R.id.start_month);
        startDate = dialogView.findViewById(R.id.start_date);
        startYear = dialogView.findViewById(R.id.start_year);
        endMonth = dialogView.findViewById(R.id.end_month);
        endDate = dialogView.findViewById(R.id.end_date);
        endYear = dialogView.findViewById(R.id.end_year);
        ImageView cancel = dialogView.findViewById(R.id.cross);
        cancel.setOnClickListener(this);
        widget.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
        widget.setOnDateChangedListener(this);
        widget.setOnRangeSelectedListener(this);
        widget.setDynamicHeightEnabled(false);
        dialogView.findViewById(R.id.done).setOnClickListener(this);
        initDates();
    }

    private void initDates() {
        widget.state().edit()
                .setMaximumDate(CalendarDay.from(LocalDate.now()))
                .setMinimumDate(CalendarDay.from(LocalDate.now().minusMonths(3)))
                .commit();

        String sMonth;
        String sDate;
        String eMonth;
        String eDate;
        String sYear;
        String eYear;
        CalendarDay startRange;
        CalendarDay endRange;

        if (getArguments() != null && getArguments().getParcelable(KEY_START_DATE) != null) {
            sMonth = HealthAlertUtils.MONTH_FORMAT.format(((CalendarDay) (getArguments().getParcelable(KEY_START_DATE))).getDate());
            sDate = HealthAlertUtils.DAY_FORMAT.format(((CalendarDay) (getArguments().getParcelable(KEY_START_DATE))).getDate());
            eMonth = HealthAlertUtils.MONTH_FORMAT.format(((CalendarDay) (getArguments().getParcelable(KEY_END_DATE))).getDate());
            eDate = HealthAlertUtils.DAY_FORMAT.format(((CalendarDay) (getArguments().getParcelable(KEY_END_DATE))).getDate());
            sYear = " " + HealthAlertUtils.YEAR_FORMAT.format(((CalendarDay) (getArguments().getParcelable(KEY_START_DATE))).getDate()) + " - ";
            eYear = " " + HealthAlertUtils.YEAR_FORMAT.format(((CalendarDay) (getArguments().getParcelable(KEY_END_DATE))).getDate());
            startRange = (CalendarDay) (getArguments().getParcelable(KEY_START_DATE));
            endRange = (CalendarDay) (getArguments().getParcelable(KEY_END_DATE));
            startSelectedDate = startRange;
        } else {
            String januaryFirst = HealthAlertUtils.DATE_FORMAT_FOR_SOCKET.format(LocalDate.now());
            sMonth = HealthAlertUtils.MONTH_FORMAT.format(LocalDate.parse(januaryFirst).minusDays(1));
            sDate = HealthAlertUtils.DAY_FORMAT.format(LocalDate.parse(januaryFirst).minusDays(1));
            eMonth = HealthAlertUtils.MONTH_FORMAT.format(LocalDate.now());
            eDate = HealthAlertUtils.DAY_FORMAT.format(LocalDate.now());
            sYear = " " + HealthAlertUtils.YEAR_FORMAT.format(LocalDate.now()) + " - ";
            eYear = " " + HealthAlertUtils.YEAR_FORMAT.format(LocalDate.parse(januaryFirst).minusDays(1));
            startRange = CalendarDay.from(LocalDate.now().minusDays(1));
            endRange = CalendarDay.from(LocalDate.now());
        }

        widget.setCurrentDate(startRange, true);
        widget.selectRange(startRange, endRange);
        startMonth.setText(sMonth.toUpperCase());
        startDate.setText(sDate);
        startYear.setText(sYear);
        endMonth.setText(eMonth.toUpperCase());
        endDate.setText(eDate);
        endYear.setText(eYear);
        currentMonthIndex = widget.getCurrentDate().getMonth();
        currentYearIndex = widget.getCurrentDate().getYear();

        widget.addDecorator(new PrimeDayDisableDecorator());
        widget.addDecorator(new SelectionDecorator());
        widget.addDecorator(new OtherMonthDateDecorator());

        widget.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                currentMonthIndex = date.getMonth();
                currentYearIndex = date.getYear();
                //Need to call decorator after every month change
                widget.addDecorator(new PrimeDayDisableDecorator());
                widget.addDecorator(new SelectionDecorator());
                widget.addDecorator(new OtherMonthDateDecorator());
            }
        });
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        startSelectedDate = date;
        setStartDateAndYearOnView(date);
    }

    private void setStartDateAndYearOnView(CalendarDay date) {
        startMonth.setText(HealthAlertUtils.MONTH_FORMAT.format(date.getDate()).toUpperCase());
        startDate.setText(HealthAlertUtils.DAY_FORMAT.format(date.getDate()));
        String year = " " + HealthAlertUtils.YEAR_FORMAT.format(date.getDate()) + " - ";
        startYear.setText(year);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cross:
                dismiss();
                break;
            case R.id.done:

                CalendarDay endSelectedDate = null;

                if (mDates.size() > 0) {
                    endSelectedDate = mDates.get(mDates.size() - 1);
                    String eYear = " " + HealthAlertUtils.YEAR_FORMAT.format(mDates.get(mDates.size() - 1).getDate());

                    endMonth.setText(HealthAlertUtils.MONTH_FORMAT.format(mDates.get(mDates.size() - 1).getDate()).toUpperCase());
                    endDate.setText(HealthAlertUtils.DAY_FORMAT.format(mDates.get(mDates.size() - 1).getDate()));
                    endYear.setText(eYear);
                }

                if (startSelectedDate == null || endSelectedDate == null) {
                    startSelectedDate = CalendarDay.from(LocalDate.now().minusDays(1));
                    endSelectedDate = CalendarDay.from(LocalDate.now());
                }
                onCalendarDateSelectedListener.OnDateSelected(startSelectedDate, endSelectedDate);
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
        mDates = dates;
        startSelectedDate = mDates.get(0);
        endSelectedDate = mDates.get(mDates.size() - 1);

        setStartDateAndYearOnView(dates.get(0));
        endMonth.setText(HealthAlertUtils.MONTH_FORMAT.format(mDates.get(mDates.size() - 1).getDate()).toUpperCase());
        endDate.setText(HealthAlertUtils.DAY_FORMAT.format(mDates.get(mDates.size() - 1).getDate()));
        String year = " " + HealthAlertUtils.YEAR_FORMAT.format(mDates.get(mDates.size() - 1).getDate());
        endYear.setText(year);
    }

    private class PrimeDayDisableDecorator implements DayViewDecorator {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            int month = day.getMonth();

            if (month == currentMonthIndex) {
                return day.getDate().isAfter(widget.getMaximumDate().getDate())
                        || day.getDate().isBefore(widget.getMinimumDate().getDate());
            } else {
                if (startSelectedDate != null && endSelectedDate != null) {
                    return !day.getDate().equals(startSelectedDate.getDate())
                            && !day.getDate().equals(endSelectedDate.getDate());
                }
                return false;
            }
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
            view.addSpan(new ForegroundColorSpan(getResources().getColor(R.color.grey)));
        }
    }

    private class SelectionDecorator implements DayViewDecorator {
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            int month = day.getMonth();
            if (month != currentMonthIndex) {
                if (startSelectedDate != null && endSelectedDate != null) {
                    return day.getDate().equals(startSelectedDate.getDate())
                            || day.getDate().equals(endSelectedDate.getDate());
                }
            }
            return false;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
        }
    }

    private class OtherMonthDateDecorator implements DayViewDecorator {
        @Override
        public boolean shouldDecorate(CalendarDay day) {

            int month = day.getMonth();
            int totalWeekCount = 7;
            if ((day.getYear() == currentYearIndex && month > currentMonthIndex)
                    || day.getYear() > currentYearIndex) {

                DayOfWeek dayOfWeek = lastDay.getDate().getDayOfWeek();
                int value = dayOfWeek.getValue();
                return day.getDay() > (totalWeekCount - value);

            } else {
                lastDay = day;
                return false;
            }
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.setDaysDisabled(true);
            view.addSpan(new RelativeSizeSpan(0f));
        }
    }


}
