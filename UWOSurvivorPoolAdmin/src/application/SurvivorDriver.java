package application;
//Do not remove these imports
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Coby Viner
 * @version 0.1 Coby Viner, Feb. 26, 2012
 */
public class SurvivorDriver {
	/**
	 * Logger to enable logging.
	 * It is only used for severe issues in this application.
	 * This is mainly because this application is designed for local use only
	 * and because this application runs sequentially, without overly complex operations.
	 */
	private final static Logger LOGGER = Logger.getLogger(Survivor.class.getName());

	public static void main(String[] args) {
		/** This is needed due to a bug within Nimbus that throws a ClassCastException.
		 * It cannot be extrinsically ameliorated.
		 * The exception causes no harm to the application in any way.
		 * This is only to prevent the error message from being output to console,
		 * whilst maintaining the ability to debug it in the future (i.e. when a bugfix is deployed).
		 * {@link http://bugs.sun.com/view_bug.do?bug_id=6785663}
		 */
//		//set up a FileHandler to write the log to a file
//		try {
//			FileHandler handler = new FileHandler(Survivor.class.getName() + ".log", false);
//			LOGGER.addHandler(handler);
//		} catch (Exception ex) {}
//
//		//suppress logger output to console
//		Logger r = Logger.getLogger("");
//		Handler[] handlers = r.getHandlers();
//		if (handlers[0] instanceof ConsoleHandler) {
//			r.removeHandler(handlers[0]);
//		}
//
//		//set the uncaught exception handler to simply log exceptions to a log file
//		Thread.setDefaultUncaughtExceptionHandler(
//				new UncaughtExceptionHandler() {
//					/* (non-Javadoc)
//					 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
//					 */
//					@Override
//					public void uncaughtException(Thread t, Throwable e) {
//						/*
//						 * Log all uncaught exceptions.
//						 * This is expected to only be due to the Nimbus bug cited above.
//						 * This is logged to allow for bug tracking.
//						 */
//						LOGGER.log(Level.SEVERE, e.getMessage(), e);
//					}
//				});
		initGui(new Survivor());
	}

	private static void initGui(final Survivor survivor) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ui.RootPanel(survivor);
			}
		});
	}
	
	/**
	 * Safely exits the program. This must be called when exiting.
	 */
	public static void exit() {
		io.io.writePropertiesTofile();
		System.exit(0);
	}
}
