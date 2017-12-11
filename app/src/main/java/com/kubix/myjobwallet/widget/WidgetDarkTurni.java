package com.kubix.myjobwallet.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.kubix.myjobwallet.R;
import com.kubix.myjobwallet.calendario.CalendarioActivity;
import com.kubix.myjobwallet.calendario.TurniActivity;

/**
 * Created by mowmo on 11/12/17.
 */

public class WidgetDarkTurni extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int i = 0; i < appWidgetIds.length; i++) {

            int appWidgetId = appWidgetIds[i];

            Intent intent1 = new Intent(context, CalendarioActivity.class);
            Intent intent2 = new Intent(context, TurniActivity.class);


            PendingIntent pending1 = PendingIntent.getActivity(context, 0, intent1, 0);
            PendingIntent pending2 = PendingIntent.getActivity(context, 0, intent2, 0);

            RemoteViews views1 = new RemoteViews(context.getPackageName(), R.layout.widget_turni_dark);
            RemoteViews views2 = new RemoteViews(context.getPackageName(), R.layout.widget_turni_dark);

            views1.setOnClickPendingIntent(R.id.btnWidTurno, pending1);
            views2.setOnClickPendingIntent(R.id.btnWidListaTurni, pending2);


            appWidgetManager.updateAppWidget(appWidgetId, views1);
            appWidgetManager.updateAppWidget(appWidgetId, views2);

        }
    }
}

