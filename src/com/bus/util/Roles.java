package com.bus.util;

public interface Roles {

	//For employee system
	public static final String EMPLOYEE_SYSTEM = "employee_system";
	public static final String EMPLOYEE_VIEW = "employee_view";
	public static final String EMPLOYEE_EDIT = "employee_edit";
	
	
	//For Score System
	public static final String SCORE_SYSTEM = "score_system";
	public static final String SCORE_HOME_VIEW = "score_home_view";
	public static final String SCORE_DETAIL_VIEW = "score_detail_view";
	public static final String SCORE_DETAIL_REMOVE_RECORD = "score_detail_remove_record";
	public static final String SCORE_ITEMS_FILE_UPLOAD = "score_fileupload_uploaditems";
	public static final String SCORE_SCORES_FILE_UPLOAD = "score_fileupload_uploadscores";
	public static final String SCORE_ITEMS_VIEW = "score_items_view";
	public static final String SCORE_ITEMS_CREATE = "score_items_create";
	public static final String SCORE_ITEMS_EDIT = "score_items_edit";
	public static final String SCORE_GIVE_SCORE= "score_items_givescore";
	public static final String SCORE_SHEET_VIEW = "score_sheet_view";
	public static final String SCORE_SHEET_ADD = "score_sheet_create";
	public static final String SCORE_SHEET_RM = "score_sheet_remove";
	public static final String Score_SHEET_RM_ST = "score_sheet_rm_st";
	public static final String SCORE_SHEET_ADD_ST = "score_sheet_add_st";
	
	
	//For HR System
	public static final String ACCOUNT_SYSTEM = "account_system";
	public static final String ACCOUNT_ACCOUNT_CREATE = "account_createaccount";
	public static final String ACCOUNT_GROUP_CREATE = "account_creategroup";
	public static final String ACCOUNT_ASSIGN_ACC_TO_GP= "account_assigngroup";
	public static final String ACCOUNT_REMOVE_ACC = "account_removeaccount";
	public static final String ACCOUNT_RESIGN_ACC = "account_resignaccount";
	public static final String ACCOUNT_REMOVE_GP = "account_removegroup";
	public static final String ACCOUNT_VIEW_ACC_GPS = "account_viewaccountgroups";
	public static final String ACCOUNT_VIEW_GP_ACTIONS = "account_viewgroupactions";
	public static final String ACCOUNT_REMOVE_GP_FROM_USER = "account_removegroupfromuser";
	public static final String ACCOUNT_ASSIGN_ACTION_TO_GP = "account_assignactiontogroup";
	
	
}
