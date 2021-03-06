package net.riverrouge.shingo.server;

import static net.riverrouge.shingo.server.TestConstants.*;

import net.riverrouge.shingo.server.api.Facade;
import net.riverrouge.shingo.server.api.GenericResponse;
import net.riverrouge.shingo.server.model.Task;

import java.util.logging.Logger;

/**
 * A fake file uploader
 */
public class TestUploadWorker implements Runnable {

  private static final Logger LOG = Logger.getLogger(TestUploadWorker.class.getName());

  void handleWork() {

    GenericResponse response =
        Facade.getTask(WORKFLOW_TYPE_NAME,  WORKFLOW_TYPE_VERSION, UPLOAD_TASK_TAG);

    Task task = response.getTask();

    if (task != null) {
      LOG.info("Handling upload.");

      String annotatedFileString = task.getExecution().getMemo().getNote("output vcf file");

      // Lets assume for testing that we did the annotation
      if (annotatedFileString != null) {
        Facade.completeTask(task.getId());
      } else {
        // TODO(ljw1001): throw an exception
        LOG.severe("Unable to find the output vcf file to upload in the execution memo");
      }
    } else {
      LOG.info("Nothing to do in test upload worker.");
    }
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used to create a thread,
   * starting the thread causes the object's <code>run</code> method to be called in that separately
   * executing thread.
   * <p/>
   * The general contract of the method <code>run</code> is that it may take any action whatsoever.
   *
   * @see Thread#run()
   */
  @Override
  public void run() {
    LOG.info("Running test upload server");
    LOG.info("*****************************");
    handleWork();
  }
}
