Dumbster fake SMTP Server
Forked from http://quintanasoft.com/dumbster/ version 1.6 by Jason Kitchen

* Works as a unit testing SMTP target (just as always)
* Now works stand-alone as an executable JAR
* Now multithreaded (by default, single-threaded for unit testing)
* Improved test coverage


EXAMPLE (SMTP unit testing fake)
public class SimpleSmtpServerTest extends TestCase {
...
  public void testSend() {
    SimpleSmtpServer server = SimpleSmtpServer.start();

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
    Iterator emailIter = server.getReceivedEmail();
    SmtpMessage email = (SmtpMessage)emailIter.next();
    assertTrue(email.getHeaderValue("Subject").equals("Test"));
    assertTrue(email.getBody().equals("Test Body"));	
  }
...  
}

EXAMPLE (SMTP fake server for QA, running on port 4444)
java -jar dumbster.jar 4444

LICENSE
=======
Under Apache 2.0 license.
