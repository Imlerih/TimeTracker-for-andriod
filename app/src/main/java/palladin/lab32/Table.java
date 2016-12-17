package palladin.lab32;

/**
 * Created by Palladin on 05.12.2016.
 */
public class Table
{
    public static final String CATEGORIES = "CATEGORIES";
    public static class Categories
    {

        public static final String COL_ID = "ID";
        public static final String COL_CATEGORY_NAME = "CATEGORY_NAME";

        public static final String CreateQuire =
                "CREATE TABLE CATEGORIES (ID INTEGER PRIMARY KEY, CATEGORY_NAME TEXT UNIQUE)";

    }

    public static final String RECORDS = "RECORDS";
    public static class Records
    {
        public static final String COL_ID = "ID";
        public static final String COL_CATEGORY_ID = "CATEGORY_ID";
        public static final String COL_START_TIME = "START_TIME";
        public static final String COL_END_TIME = "END_TIME";
        public static final String COL_PERIOD = "PERIOD";
        public static final String COL_DESCRIPTION = "DESCRIPTION";

        public static final String CreateQuire =
                "CREATE TABLE RECORDS (ID INTEGER PRIMARY KEY" +
                        ", CATEGORY_ID INTEGER" +
                        ", START_TIME INTEGER" +
                        ", END_TIME INTEGER" +
                        ", PERIOD INTEGER" +
                        ", DESCRIPTION TEXT)";

    }
}