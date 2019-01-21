import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.PrintWriter
import java.net.Socket

fun main(args: Array<String>) {
    HttGetpClientWithRst().run(args)
}

class HttGetpClientWithRst {

    private val log: Logger = LoggerFactory.getLogger(HttGetpClientWithRst::class.java)

    fun run(args: Array<String>) {
        if (args.size != 3) {
            log.error("need args: host, port and path")
            return
        }

        val host = args[0]
        val port = args[1].toInt()
        val path = args[2]

        log.info("host: $host, port: $port")
        val socket = Socket(host, port).apply { setSoLinger(true, 0) }

        log.info("writing request...")
        log.info("path: $path")
        with(PrintWriter(socket.getOutputStream())) {
            println("GET $path HTTP/1.0")
            println()
            flush()
        }
        log.info("wrote request")

        log.info("starting to read...")
        val inputStream = socket.getInputStream()
        val byteArray = ByteArray(256)
        val read = inputStream.read(byteArray)
        log.info("got $read bytes")
        log.info("closing socket...")
        socket.close()
        log.info("socket closed.")
    }
}
