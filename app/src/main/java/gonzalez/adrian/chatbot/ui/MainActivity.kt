package gonzalez.adrian.chatbot.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import gonzalez.adrian.chatbot.R
import gonzalez.adrian.chatbot.data.Message
import gonzalez.adrian.chatbot.utils.Constans.OPEN_GOOGLE
import gonzalez.adrian.chatbot.utils.Constans.OPEN_SEARCH
import gonzalez.adrian.chatbot.utils.Constans.RECEIVE_ID
import gonzalez.adrian.chatbot.utils.Constans.SEND_ID
import gonzalez.adrian.chatbot.utils.Time
import kotlinx.coroutines.*
import gonzalez.adrian.chatbot.utils.BotResponse

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    var messagesList = mutableListOf<Message>()

    private lateinit var adapter: MessangingAdapter
    private val botList = listOf("peter", "francesca", "luigi", "igor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclearView()

        clickEvents()

        val random = (0..3).random()
        customBotMessage("hello! today you're speaking with ${botList[random]}, how may i help?")
    }

    private fun customBotMessage(message: String) {

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                messagesList.add(Message(message, RECEIVE_ID, timeStamp))
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))

                rv_messages.scrollToPosition(adapter.itemCount -1)
            }
        }

    }

    private fun recyclearView() {
        adapter = MessangingAdapter()
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun clickEvents(){
        btn_send.setOnClickListener{
            sendMessage()
        }

        et_message.setOnClickListener{
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main){
                    rv_messages.scrollToPosition(adapter.itemCount - 1)
                }

            }
        }
    }

    private fun sendMessage() {
        val message = et_message.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty())
            messagesList.add(Message(message, SEND_ID, timeStamp))
        et_message.setText("")

        adapter.insertMessage(Message(message, SEND_ID, timeStamp))
        rv_messages.scrollToPosition(adapter.itemCount -1)

        botResponse(message)

    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {

            delay(1000)

            withContext(Dispatchers.Main){
                val response = BotResponse.basicResponses(message)

                messagesList.add(Message(response, RECEIVE_ID, timeStamp))
                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))

                rv_messages.scrollToPosition(adapter.itemCount - 1)

                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }
                }
            }
        }

    }
}