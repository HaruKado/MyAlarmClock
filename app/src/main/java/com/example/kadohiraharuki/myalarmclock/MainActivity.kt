package com.example.kadohiraharuki.myalarmclock

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat.getSystemService
import android.view.WindowManager.LayoutParams.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.text.DateFormat
import java.text.ParseException
import java.util.*
import java.util.regex.Pattern

class MainActivity : AppCompatActivity()
    , SimpleAlertDialog.OnClickListener{//ダイアログのボタンが押されたときの処理Dialogs.ktのクラス読み出し
    /*,DatePickerFragment.OnDateSelectedListener//
    ,TimePickerFragment.OnTimeSelectedListener {

    override fun onSelected(year: Int, month: Int, date: Int) {
        val c = Calendar.getInstance()
        c.set(year, month, date)
        dateText.text = DateFormat.format("yyyy/MM/dd", c)
    }

    override fun onSelected(hourOfDay: Int, minute: Int) {
        timeText.text = "%1$02d:%2$02d".format(hourOfDay, minute)
    }
    */
    //Dialogs.kt内のsetPositiveButtonクラスであれば終了
    override fun onPositiveClick() {
        finish()
    }

    //Dialogs.kt内のsetNegativeButtonクラスであれば５分後に同じ要求画面
    override fun onNegativeClick() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE, 5)
        setAlarmManager(calendar)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //エクストラのonReceiveにtrueが指定されて起動された場合にダイアログを表示するようにする
        if (intent?.getBooleanExtra("onReceive", false) == true) {
            /* // スリープを解除して画面を表示する昨日　
               when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
                    window.addFlags(FLAG_TURN_SCREEN_ON or FLAG_SHOW_WHEN_LOCKED)
                else -> window.addFlags(FLAG_TURN_SCREEN_ON or FLAG_SHOW_WHEN_LOCKED
                        or FLAG_DISMISS_KEYGUARD)
            }*/
            //SimpleAlertDialogのインスタンスを生成
            val dialog = SimpleAlertDialog()
            //showメソッドでdialogを表示
            dialog.show(supportFragmentManager, "alert_dialog")
            }

            setContentView(R.layout.activity_main)

            setAlarm.setOnClickListener {
               /* val date = "${dateText.text} ${timeText.text}".toDate()
                when {
                    date != null -> {
                        val calendar = Calendar.getInstance()
                        calendar.time = date
                        setAlarmManager(calendar)
                        toast("アラームをセットしました")
                    }
                    else -> {
                        toast("日付の形式が正しくありません")
                    }
                }*/

            //ボタンをタップしたと時にcalendarクラスで現在の時間より5秒後の時間を作成
            //getInstanceでcalendarクラスのインスタンスを取得
            val calendar = Calendar.getInstance()
            //calendarへの時間設定はtimeInMillsプロパティでアクセス、System.currentTimeMillisメソッドで現在の時刻設定
            calendar.timeInMillis = System.currentTimeMillis()
            //addメソッドでCalendarに設定している時刻を編集、引数には編集したい場所と値を渡す
            calendar.add(Calendar.SECOND, 5)
            //setAlarmManager関数にcalendarクラスのインスタンスを渡す
            setAlarmManager(calendar)

            }



            //ボタンをタップしたらアラームをキャンセル
            cancelAlarm.setOnClickListener {
                cancelAlarmManager()
            }
            /*dateText.setOnClickListener {
                val dialog = DatePickerFragment()
                dialog.show(supportFragmentManager, "date_dialog")
            }
            timeText.setOnClickListener {
                val dialog = TimePickerFragment()
                dialog.show(supportFragmentManager, "time_dialog")
            }*/

        }



        //このアノテーションをつけることにより、android4.1でも使えるようにする
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun setAlarmManager(calendar: Calendar) {
            //AlarmManager(Androidのシステム)クラスのインスタンスを取得
            //getSystemServiceメソッドに引数Context.ALARM_SERVICEを渡して行う。
            //このメソッドの戻り値はAny型なので、AlarmManagerに変換
            val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            //アラーム時刻になったときにシステムから発行されるインテントを作成
            val intent = Intent(this, AlarmBroadcastReceiver::class.java)
            //AlarmManagerに登録するため、作成したインテントを指定してペンディングインテントを作成
            val pending = PendingIntent.getBroadcast(this, 0, intent, 0)

            when {
            //Android5.0Lollipop
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                    //AlarmManager.AlarmClockInfoクラスのインスタンスを用意し、設定するアラームの時刻とアラーム設定のためのインテント指定
                    val info = AlarmManager.AlarmClockInfo(calendar.timeInMillis, null)
                    am.setAlarmClock(info, pending)
                }
            //Android4.4KitKat
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                    am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pending)
                }
            //それ以前の機種
                else -> {
                    am.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pending)
                }
            }
        }


        //キャンセル処理の追加
        private fun cancelAlarmManager() {
            val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlarmBroadcastReceiver::class.java)
            val pending = PendingIntent.getBroadcast(this, 0, intent, 0)
            //AlarmManagerのcancelメソッドを使い、引数にはキャンセルしたいインテントと同じものを渡す
            am.cancel(pending)
        }

        /*fun String.toDate(pattern: String = "yyyyy/MM/dd HH:mm"): Date? {
            val sdFormat = try {
                SimpleDateFormat(pattern)
            } catch (e: IllegalArgumentException) {
                null
            }
            val date = sdFormat?.let {
                try {
                    it.parse(this)
                } catch (e: ParseException) {
                    null
                }
            }
            return date
        }*/
    }

