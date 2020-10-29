//sabuj.jena@logixal.com

package kafkautil.logixal.com

import java.util.Properties

import org.apache.kafka.clients.producer._

import com.typesafe.config._

object Producer  {

  private val props = builProperties()

  def publishToKafka(topic: String, key: String, value: String): Unit ={

    val producer = new KafkaProducer[String, String](props)

    val callback = new Callback {
      override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
        Option(exception) match {
          case Some(err) => println(s"Failed to produce: $err")
          case None =>  println(s"Produced record at $metadata")
        }
      }
    }
    val record = new ProducerRecord[String, String](topic, key, value)
    producer.send(record, callback)
    producer.flush()
    producer.close()

  }

  private def builProperties(): Properties = {
    val conf = ConfigFactory.load()
    val props: Properties = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, conf.getString("my-app.kafka.producer.bootstrap-servers-config"))
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props
  }

}
