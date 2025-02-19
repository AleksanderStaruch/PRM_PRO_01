package com.example.prm_pro_01

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import java.io.*

class MainActivity : AppCompatActivity() {

    private val fileName:String = "data.csv"

    companion object{
        var debtorList = mutableListOf<Debtor>()

        fun addDebtor(name:String, debt:Double){
            debtorList.add(Debtor(name, debt))
        }

        fun changeDebtor(position:Int, name:String, debt:Double){
            debtorList[position].name = name
            debtorList[position].debt = debt
        }
    }

//    private fun saveData(){
//        val fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
//        try {
//            for(debtor in debtorList){
//                val data: String = debtor.name+","+debtor.debt.toString()
//                fileOutputStream.write(data.toByteArray())
//                fileOutputStream.flush()
//            }
//        }catch (e: IOException){
//            e.printStackTrace()
//        }finally {
//            fileOutputStream.close()
//        }
//    }
//
//    private fun loadData(){
//        var fileInputStream: FileInputStream = openFileInput(fileName)
//        try {
//            fileInputStream
//            val bufferedReader = BufferedReader(InputStreamReader(fileInputStream))
//            debtorList.clear()
//            bufferedReader.forEachLine {
//                val tmp = it.split(",")
//                val name = tmp[0]
//                val debt = tmp[1].toDouble()
//                debtorList.add(Debtor(name,debt))
//            }
//        }catch (e: IOException){
//            e.printStackTrace()
//        }finally {
//            fileInputStream.close()
//        }
//    }

    private fun sumOfAllDebts(list:List<Debtor>): Double {
        var sum=0.0
        for(i in list){
            sum += i.debt
        }
        return sum
    }

    private fun deleteDebtor(position:Int,listview: ListView){
        debtorList.removeAt(position)
        listview.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, debtorList)
    }

    private fun update(textview: TextView,listview: ListView){
        textview.text = "Debtors borrowed from you: "+sumOfAllDebts(debtorList)
        listview.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, debtorList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()

        val addbuttpn = findViewById<Button>(R.id.w1_add_button)
        val listview = findViewById<ListView>(R.id.w1_listView)
        val textview = findViewById<TextView>(R.id.w1_textView2)

        update(textview,listview)

        //change
        listview.setOnItemClickListener{ _, _, position, _ ->
            val intent = Intent(this, DebtorActivity::class.java).apply {
                putExtra("msg","change")
                putExtra("name",debtorList[position].name)
                putExtra("debt",debtorList[position].debt.toString())
                putExtra("position",position.toString())
            }
            startActivity(intent)
        }

        //delete
        listview.setOnItemLongClickListener{ _, _, position, _ ->
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder
                .setTitle("Delete?")
                .setMessage("Are you sure you want delete?")
                .setPositiveButton("Yes"){ _, _ ->
                    deleteDebtor(position,listview)
                    update(textview,listview)
                }
                .setNegativeButton("No"){ _, _ -> }

            val dialog = dialogBuilder.create()
            dialog.show()

            true
        }

        //add
        addbuttpn.setOnClickListener {
            val intent = Intent(this, DebtorActivity::class.java).apply {
                putExtra("msg","add")
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val listview = findViewById<ListView>(R.id.w1_listView)
        val textview = findViewById<TextView>(R.id.w1_textView2)
        update(textview,listview)
    }

}
