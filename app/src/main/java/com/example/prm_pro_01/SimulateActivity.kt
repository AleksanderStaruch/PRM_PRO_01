package com.example.prm_pro_01

import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.interest_dialog.view.*
import java.lang.Thread.sleep
import kotlin.concurrent.thread
import kotlin.math.round


class SimulateActivity : AppCompatActivity() {

    private var interest = 0
    private var sumInterest = 0.0
    private var payment = 0.0

    fun dialogInfo(sumInterest:Double,interest:Int,payment:Double){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle("Info")
            .setMessage("Sum of interest is $sumInterest, with parameters: interest=$interest% and payment=$payment")
            .setPositiveButton("OK"){ _, _ -> }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    fun dialogSlider(p:Int, seekBar: SeekBar, textView: TextView){
        var tmp = p

        val inflater = layoutInflater
        val layoutDialog = inflater.inflate(R.layout.interest_dialog, null)
        val buttonCancel = layoutDialog.dialog_cancel
        val buttonOk = layoutDialog.dialog_ok
        val buttonUp = layoutDialog.dialog_increase
        val buttonDown = layoutDialog.dialog_decrease
        val text = layoutDialog.dialog_interest_text
        text.text = tmp.toString()

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Info").setView(layoutDialog)
        val dialog = dialogBuilder.create()

        buttonCancel.setOnClickListener {
            dialog.hide()
        }
        buttonOk.setOnClickListener {
            interest = tmp
            seekBar.progress = tmp
            textView.text = "$tmp%"
            dialog.hide()
        }

        buttonUp.setOnClickListener {
            if(tmp < 100){
                tmp+=1
                text.text = tmp.toString()
            }
        }

        buttonUp.setOnLongClickListener {
            thread{
                while (buttonUp.isPressed){
                    if(tmp < 100){
                        tmp+=1
                        runOnUiThread {
                            text.text = tmp.toString()
                        }
                    }
                    sleep(100)
                }
            }
            true
        }

        buttonDown.setOnClickListener {
            if(tmp > 0){
                tmp-=1
                text.text = tmp.toString()
            }
        }

        buttonDown.setOnLongClickListener {
            thread{
                while (buttonDown.isPressed){
                    if(tmp > 0){
                        tmp-=1
                        runOnUiThread {
                            text.text = tmp.toString()
                        }
                    }
                    sleep(100)
                }
            }
            true
        }

        dialog.show()
    }

    fun roundDouble(d:Double):Double{
        val tmp = d*100
        return round(tmp) /100
    }

    fun animation(debtorWalletImage:ImageView, myWalletImage:ImageView, dollarImage:ImageView){

        val x1 = debtorWalletImage.x
        val y1 = debtorWalletImage.y
        val x2 = myWalletImage.x
        val y2 = myWalletImage.y
//        Log.e("ANIMATION",startAngle.toString())
//        Log.e("ANIMATION x1",x1.toString())
//        Log.e("ANIMATION y1",y1.toString())
//        Log.e("ANIMATION x2",x2.toString())
//        Log.e("ANIMATION y2",y2.toString())
//        Log.e("ANIMATION r",r.toString())
        val path = Path().apply {
//            arcTo(0f, 0f, 1000f, 1000f, 270f, -180f, true)
//            arcTo(x1,y1,x2,y2,270f,-270f,true)
            arcTo(x1+20,y1,x2+20,y2+200,180f,180f,true)
//            arcTo(x1,y1,x1+r,y1-r,90f,180f,true)
//            arcTo(centerX-180,centerY,centerX+180,centerX,180f,180f,true)
        }

        val anim = ObjectAnimator.ofFloat(dollarImage, View.X, View.Y, path).apply {
            duration = 1000
            start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simulate)

        val name = intent.getStringExtra("name")
        var debt= intent.getStringExtra("debt").toDouble()

        val infoText = findViewById<TextView>(R.id.w3_info_text)
        val seekBar = findViewById<SeekBar>(R.id.w3_seekBar)
        val percent = findViewById<TextView>(R.id.w3_interest)
        val amount = findViewById<EditText>(R.id.w3_amount_text)
        val button = findViewById<Button>(R.id.w3_start_stop_button)
        val restPayment = findViewById<TextView>(R.id.w3_rest_payment_text)
        val debtorWallet = findViewById<TextView>(R.id.w3_debtor_wallet_text)

        val debtorWalletImage = findViewById<ImageView>(R.id.w3_debtor_wallet_iamge)
        val myWalletImage = findViewById<ImageView>(R.id.w3_my_wallet_iamge)
        val dollarImage = findViewById<ImageView>(R.id.w3_dollar_image)


        infoText.text = "$name owns you $debt."
        percent.text = "$interest%"
        restPayment.text = "Rest to pay: $debt"
        debtorWallet.text = "$name's wallet"

        thread{
            while(debt > 0){
                if(button.text == "STOP") {
                    payment = amount.text.toString().toDouble()
                    debt -= payment

                    if(debt < 0){ debt = 0.0 }

                    val tmp = roundDouble(debt * (interest.toDouble() / 100))
                    sumInterest += tmp
                    debt += tmp

                    runOnUiThread{
                        restPayment.text = "Rest to pay: $debt"
                        animation(debtorWalletImage, myWalletImage, dollarImage)
                    }
                }
                sleep(1000)
            }
            runOnUiThread{ dialogInfo(sumInterest,interest,payment) }
        }

        percent.setOnLongClickListener {
            dialogSlider(interest,seekBar,percent)
            false
        }

        seekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                percent.text = "$progress%"
                interest = progress
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        button.setOnClickListener {
            if (amount.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Fill the gaps", Toast.LENGTH_LONG).show()
            }else{
                if (button.text == "STOP"){
                    button.text = "START"
                }else{
                    button.text = "STOP"
                }
            }
        }

    }
}
