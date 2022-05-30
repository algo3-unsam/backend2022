package ar.edu.unsam.algo

interface MailSender {
    fun sendMail(mail: Mail)
}

/*class appMailSender : MailSender{
    override fun sendMail(mail: Mail) {
        println(mail)
    }

}*/

data class Mail(val from:String, val to: String, val subject: String, val content: String)