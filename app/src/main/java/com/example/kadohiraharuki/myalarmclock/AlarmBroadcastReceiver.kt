package com.example.kadohiraharuki.myalarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class AlarmBroadcastReceiver: BroadcastReceiver() {
    //ブロードキャストインテントを受け取った時呼ばれる、AlarmManagerからBroadcastReceiverにインテントが送られて来る
    override fun onReceive(context: Context?, intent: Intent?){
        /*
        //toastクラスを使ってウィンドウの前面に一定時間メッセージを表示
        context?.toast("アラームを受信しました")*/

        //作成したSimpleAlertDialogを表示する、AlarmBroadcastReceiverでアラーム時間になったことを受け取ったら、ダイアログを表示
        //アクティビティを呼ぶためのインテントを作成
        val intent = Intent(context, MainActivity::class.java)
                //インテントに追加情報を格納
                .putExtra("onReceive" ,true)
                //BroadcastReceiverからアクテビティを呼び出すには、インテントにIntent.FLAG_ACTIVITY_NEW_TASKフラッグをつけておく必要がある
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //onReceiveメソッドに渡される
        context?.startActivity(intent)
        //Ankoを使って上記のコードを書き換え
        /*context?.run{
            startActivity(intentFor<MainActivity>("onReceive" to true).newTask())
        }*/


    }

}