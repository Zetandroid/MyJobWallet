package com.kubix.myjobwallet.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.kubix.myjobwallet.R;
import com.kubix.myjobwallet.RiepilogoActivity;
import com.kubix.myjobwallet.entrate.EntrateAggiungiActivity;
import com.kubix.myjobwallet.note.NoteAggiungiActivity;
import com.kubix.myjobwallet.spese.SpeseAggiungiActivity;

public class WidgetLight extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int i = 0; i < appWidgetIds.length; i++) {

            int appWidgetId = appWidgetIds[i];

            Intent intent1 = new Intent(context, EntrateAggiungiActivity.class);
            Intent intent2 = new Intent(context, SpeseAggiungiActivity.class);
            Intent intent3 = new Intent(context, NoteAggiungiActivity.class);
            Intent intent4 = new Intent(context, RiepilogoActivity.class);

            PendingIntent pending1 = PendingIntent.getActivity(context, 0, intent1, 0);
            PendingIntent pending2 = PendingIntent.getActivity(context, 0, intent2, 0);
            PendingIntent pending3 = PendingIntent.getActivity(context, 0, intent3, 0);
            PendingIntent pending4 = PendingIntent.getActivity(context, 0, intent4, 0);

            RemoteViews views1 = new RemoteViews(context.getPackageName(), R.layout.widget_light);
            RemoteViews views2 = new RemoteViews(context.getPackageName(), R.layout.widget_light);
            RemoteViews views3 = new RemoteViews(context.getPackageName(), R.layout.widget_light);
            RemoteViews views4 = new RemoteViews(context.getPackageName(), R.layout.widget_light);

            views1.setOnClickPendingIntent(R.id.btnWidEntrata, pending1);
            views2.setOnClickPendingIntent(R.id.btnWidSpesa, pending2);
            views3.setOnClickPendingIntent(R.id.btnWidMemo, pending3);
            views4.setOnClickPendingIntent(R.id.btnWidRiepilogo, pending4);

            appWidgetManager.updateAppWidget(appWidgetId, views1);
            appWidgetManager.updateAppWidget(appWidgetId, views2);
            appWidgetManager.updateAppWidget(appWidgetId, views3);
            appWidgetManager.updateAppWidget(appWidgetId, views4);
        }
    }
}




