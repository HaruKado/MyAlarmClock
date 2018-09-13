package com.example.kadohiraharuki.myalarmclock


import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.net.sip.SipSession
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import org.jetbrains.anko.toast
import java.util.*
import javax.sql.RowSetListener

//ダイアログを使うためにDialogFragmentクラスを継承したクラスを作成
class SimpleAlertDialog : DialogFragment(){

    //スヌーズ機能追加
    interface OnClickListener {
        fun onPositiveClick()
        fun onNegativeClick()
    }
    //lateinitをつけてあとで　onAttachメソッドで初期化
    private lateinit var listener: OnClickListener

    override fun onAttach(context: Context?){
        //SimpleAlertDialogが呼ばれたときに実行される
        super.onAttach(context)
        if(context is SimpleAlertDialog.OnClickListener){
            listener = context
        }

    }
    //onCreateDialogメソッドをオーバーライド
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context
        //contextがnullだった場合の事前処理
        if(context == null)
            //nullの場合は親クラスのonCreateDialogを実行してその結果を返す
            return super.onCreateDialog(savedInstanceState)
        //AlertDialog.Builderのインスタンスを生成し、それを使って設定する
        val builder = AlertDialog.Builder(context).apply{
            //setMessageメソッドでダイアログに表示する文字列を設定
            setMessage("時間になりました！")
            //ダイアログに表示する1番目のボタンを設定
            setPositiveButton("起きる"){ dialog, which ->

                //listener.onPositiveClick()
                //toastクラスを使ってウィンドウの前面に一定時間メッセージを表示
                context.toast("起きるがクリックされました")
            }
            //ダイアログに表示する1番目のボタンを設定
            setNegativeButton("あと5分"){ dialog, which ->

                //listener.onNegativeClick()
                //toastクラスを使ってウィンドウの前面に一定時間メッセージを表示
                context.toast("あと5分がクリックされました")
            }
        }
        return builder.create()

    }
}
//日付を選択するダイアログを作る
//DatePickerDialogを使う場合DialogFragmentを継承, DatePickerDialog.OnDateSetListenerインターフェイスを実装
/*class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener{

    interface  OnDateSelectedListener{
        //年、月、日を引数にとるonSelected関数
        fun onSelected(year: Int, month: Int, date: Int)
    }

    private lateinit var listener: OnDateSelectedListener

    override fun onAttach(context: Context?){
        super.onAttach(context)
        if(context is OnDateSelectedListener){
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val date = c.get(Calendar.DAY_OF_MONTH)
        //インスタンスを生成して返す
        return DatePickerDialog(context, this, year, month, date)
    }
    //日付が選択されたときに呼ばれる
    override fun onDateSet(view: DatePicker, year: Int, month: Int, date: Int){
        listener.onSelected(year, month, date)
    }
}
//時刻を選択するダイアログ
//TimePickerDialog.OnTimeSetListenerを実装
class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    interface OnTimeSelectedListener {
        fun onSelected(hourOfDay: Int, minute: Int)
    }

    private lateinit var listener: OnTimeSelectedListener

    override fun onAttach(conetext: Context?) {
        super.onAttach(context)
        //
        if (context is TimePickerFragment.OnTimeSelectedListener) {
            //キャストが必要？
            listener = context as OnTimeSelectedListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        //TimePickerDialogのインスタンスを返す
        return TimePickerDialog(context, this, hour, minute, true)
    }

    //TimePickerDialogで日時が選択されたら、onTimeSetメソッドが呼ばれる
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener.onSelected(hourOfDay, minute)
    }
}
        */