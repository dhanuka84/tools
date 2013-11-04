package org.wso2.developerstudio.appfactory.ui.views;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.wso2.developerstudio.appfactory.ui.Activator;
import org.wso2.developerstudio.appfactory.ui.utils.Messages;

public class AppfactoryConsoleView {
 
   private MessageConsoleStream out;
   private  MessageConsole myConsole;
	
	public AppfactoryConsoleView() {
		   myConsole = findConsole(Messages.AppfactoryConsoleView_0);
		 MessageConsoleStream out = myConsole.newMessageStream();
		 setOut(out);
	}
	
	public MessageConsoleStream getNewMsgStream(){
		return  myConsole.newMessageStream();
	}
	
	
	public void loadConsole(MessageConsole myConsole){
		     IWorkbench wb = PlatformUI.getWorkbench();
		     IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		     IWorkbenchPage page = win.getActivePage();
			 String id = IConsoleConstants.ID_CONSOLE_VIEW;
			 IConsoleView view;
			try {
				view = (IConsoleView) page.showView(id);
				view.display(myConsole);
				
			} catch (PartInitException e) {
				e.printStackTrace();
	   }
	}
	
	
	private MessageConsole findConsole(String name) {
	      ConsolePlugin plugin = ConsolePlugin.getDefault();
	      IConsoleManager conMan = plugin.getConsoleManager();
	      IConsole[] existing = conMan.getConsoles();
	      for (int i = 0; i < existing.length; i++)
	         if (name.equals(existing[i].getName()))
	            return (MessageConsole) existing[i];
	      MessageConsole myConsole = new MessageConsole(name, this.getImageDescriptor());
	      conMan.addConsoles(new IConsole[]{myConsole});
	      return myConsole;
	   }


	public void setOut(MessageConsoleStream out) {
		this.out = out;
	}


	public MessageConsoleStream getOut() {
		return out;
	}
	
	private ImageDescriptor getImageDescriptor() {
		return  Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, Messages.AppfactoryConsoleView_1);
	}

}
