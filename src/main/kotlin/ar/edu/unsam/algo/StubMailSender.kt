package ar.edu.unsam.algo

object StubMailSender : MailSender{
    val mailsEnviados: MutableList<Mail> = mutableListOf()

    override fun sendMail(mail: Mail) {
        mailsEnviados.add(mail)
    }

    fun reset(){
        mailsEnviados.clear()
    }
}