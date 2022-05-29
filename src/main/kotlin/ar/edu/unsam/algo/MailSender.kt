package ar.edu.unsam.algo

interface MailSender {
    fun sendMail(mail: Mail)
}

class MailSenderAppMundo(): MailSender{
    override fun sendMail(mail: Mail) {}


}

data class Mail(val from: String, val to: String, val subject: String, val content: String)


object ServiceLocator {
    lateinit var mailSender: MailSender
}