package com.douzon.mysite.action.guestbook;

import com.douzon.mvc.action.AbstractActionFactory;
import com.douzon.mvc.action.Action;
import com.douzon.mysite.action.main.IndexAction;

public class GuestBookActionFactory extends AbstractActionFactory{

	@Override
	public Action getAction(String actionName) {
		Action action = null;
		
		if("guestbookform".equals(actionName)) {
			action = new GuestBookFormAction();
		}
		else if("insert".equals(actionName)) {
			action = new InsertAction();
		}
		else if("deleteform".equals(actionName)) {
			action = new DeleteFormAction();
		}
		else if("delete".equals(actionName)) {
			action = new DeleteAction();
		}
		else if("ajax".equals(actionName)) {
			action = new AjaxAction();
		}
		else if("ajax-list".equals(actionName)) {
			action = new AjaxListAction();
		}
		else if("ajax-delete".equals(actionName)){
			action = new AjaxDeleteAction();
		}
		else if("ajax-insert".equals(actionName)) {
			action = new AjaxInsertAction();
		}
		else {
			action = new IndexAction();
		}
		
		return action;
	}

}
