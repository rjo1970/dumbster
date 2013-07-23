Dumbster fake SMTP Server
Forked from http://quintanasoft.com/dumbster/ version 1.6 by Jason Kitchen

* Works as a single-threaded unit testing SMTP target
* Works as a multi-threaded unit testing SMTP target
* API change- returns an Array of messages rather than an Iterator
* API change- RollingMailStore implements MailStore keeps rolling 100 msgs.
* API change- EMLMailStore persists mail to files
* API change- SmtpServer you can inject your own MailStore
              implementation.
* API change- SmtpServer configured via ServerOptions

* Now works stand-alone as an executable JAR
* Improved test coverage
* telnet to smtp server and use "list" command to view number of msgs
* use list command with an index 0..(size-1) of messages to view a message

EXAMPLE (SMTP unit testing fake)
public class SmtpServerTest extends TestCase {
...
  public void testSend() {
    SmtpServer server = SmtpServerFactory.startServer();

    try {
      // Submits an email using javamail to the email server listening on
      // port 25 
      // (method not shown here). Replace this with a call to your app
      // logic.
      sendMessage(25, "sender@here.com", "Test", "Test Body",
"receiver@there.com");
    } catch(Exception e) {
      e.printStackTrace();
      fail("Unexpected exception: "+e);
    }

    server.stop();

    assertTrue(server.getReceivedEmailSize() == 1);
    MailMessage message = server.getMessage(0);
    assertEquals("Test", email.getHeaderValue("Subject"));
    assertEquals("Test Body", email.getBody());	
  }
...  
}

EXAMPLE (SMTP fake server for QA, running on port 4444)
java -jar dumbster.jar 4444

For more help use the command:
java -jar dumbster.jar --help


LICENSE
=======
Under Apache 2.0 license.
