package lutz.guillermo.chatbot.utils

import lutz.guillermo.chatbot.utils.Constans.OPEN_GOOGLE
import lutz.guillermo.chatbot.utils.Constans.OPEN_SEARCH
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat


object BotResponse {
    fun basicResponses(_message: String): String{
        val random = (0..2).random()
        val message =_message.toLowerCase()

        return when {
            //Flips a coin
            message.contains("flip") && message.contains("coin") -> {
                val r = (0..1).random()
                val result = if (r == 0) "heads" else "tails"
                "I flipped a coin and it leanded on $result"
            }
            message.contains("solve") -> {
                val equation: String? = message.substringAfterLast("solve")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    "$answer"
                } catch (e: Exception){
                    "sorry, i can't solve thant."
                }
            }

            message.contains("heloo") -> {
                when (random) {
                    0 -> "Hello there!"
                    1 -> "sup"
                    2 -> "Buongiorno!"
                    else -> "error" }
            }


        message.contains("time") && message.contains("?")-> {
            val timeStamp = Timestamp(System.currentTimeMillis())
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val date = sdf.format(Date(timeStamp.time))

            date.toString()
        }

        message.contains("open") && message.contains("googlge")-> {
            OPEN_GOOGLE
        }

        message.contains("search")-> {
            OPEN_SEARCH
        }
        else -> {
            when (random) {
                0 -> "i don't understand..."
                1 -> "try asking me something different"
                    2 -> "idk"
                else -> "erro"
            }
         }
      }
   }
}

