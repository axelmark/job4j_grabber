package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.Post;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;
import java.util.List;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class PostGrab implements Grab {
    @Override
    public void init() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        JobDetail job = newJob(GrabJob.class).build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(5)
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        scheduler.shutdown();
    }

    public static class GrabJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            HabrCareerParse parse = new HabrCareerParse(new HabrCareerDateTimeParser());
            List<Post> posts = parse.list("https://career.habr.com/vacancies/java_developer?page=");
        }
    }
}
