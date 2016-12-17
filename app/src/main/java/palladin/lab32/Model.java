package palladin.lab32;

import java.util.List;

/**
 * Created by Palladin on 14.12.2016.
 */
public class Model
{
    public static class Category
    {
        public String Category;
        public int Id;

        public long StartRecordTime = 0;
        public Category(String Category, int Id, long StartRecordTime)
        {
            this.Category = Category;
            this.Id = Id;
            this.StartRecordTime = StartRecordTime;
        }
    }

    public static class Record
    {
        public String Category, Description;
        public long StartTime, EndTime;
        public int Id;

        public Record(String Category, int Id, long StartTime, long EndTime, String description)
        {
            this.Category = Category;
            this.Id = Id;
            this.StartTime = StartTime;
            this.EndTime = EndTime;
            this.Description = description;
        }
    }

    public static class Statistics
    {
        public int SummaryTime;
        public String Category;
        public int Id, Count;

        public Statistics(String Category, int Id, int time, int count)
        {
            this.Category = Category;
            this.Id = Id;
            this.SummaryTime = time;
            this.Count = count;
        }
    }

}


