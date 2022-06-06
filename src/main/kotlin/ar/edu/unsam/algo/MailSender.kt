package ar.edu.unsam.algo

const val direccionCorreoApp: String = "app@gmail.com"

interface MailSender {
    fun sendMail(mail: Mail)
}


data class Mail(val from:String, val to: String, val subject: String, val content: String)