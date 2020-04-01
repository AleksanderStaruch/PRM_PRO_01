package com.example.prm_pro_01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class DebtorActivity : AppCompatActivity() {

    private fun cancelDialog(): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle("Cancel?")
            .setMessage("Are you sure you want cancel? (Current changes won't be saved)")
            .setPositiveButton("Yes"){ _, _ -> finish() }
            .setNegativeButton("No"){ _, _ ->}

        return dialogBuilder.create()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debtor)

        val saveButton = findViewById<Button>(R.id.w2_save_button)
        val cancelButton = findViewById<Button>(R.id.w2_cancel_button)
        val simulateButton = findViewById<Button>(R.id.w2_simulate_button)

        val nameValue = findViewById<EditText>(R.id.w2_name_text)
        val debtValue = findViewById<EditText>(R.id.w3_amount_text)


        val msg = intent.getStringExtra("msg")
        var name = ""
        var debt = ""

        if (msg == "change"){
            name = intent.getStringExtra("name")
            debt = intent.getStringExtra("debt")
            nameValue.setText(name)
            debtValue.setText(debt)
        }else{
            //(msg == "add")
            simulateButton.isEnabled = false
            simulateButton.isClickable = false
        }

        saveButton.setOnClickListener {
            if(! nameValue.text.isEmpty() && ! debtValue.text.isEmpty()){
                val name = nameValue.text.toString()
                val debt = debtValue.text.toString().toDouble()
                when(intent.getStringExtra("msg")){
                    "add"->{
                        MainActivity.addDebtor(name, debt)
                    }
                    "change"->{
                        val position = intent.getStringExtra("position").toInt()
                        MainActivity.changeDebtor(position,name,debt)
                    }
                }
                finish()

                Toast.makeText(applicationContext,"Saved", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(applicationContext,"Fill the gaps, or cancel.", Toast.LENGTH_LONG).show()
            }

        }

        cancelButton.setOnClickListener {
            cancelDialog().show()
        }

        simulateButton.setOnClickListener {
            val intent = Intent(this, SimulateActivity::class.java).apply {
                putExtra("name",name)
                putExtra("debt",debt)
            }
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        cancelDialog().show()
    }
}
