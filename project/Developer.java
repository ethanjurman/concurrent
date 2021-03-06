import java.util.Random;

class Developer extends Thread {

    protected Random rng;
    protected Team team;
    private int max;

    public Developer(String name, Team t, int max) {
        super(name);
        this.team = t;
        this.max = max;
        rng = new Random();
    }

    /*
     * Conduct a standard day for the developer
     */
    public void run() {
        try {
            arrive();

            //Wait for the daily developer standup
            team.getStandupBarrier().await();
            int lunchtime = 2400;

            working(lunchtime, false);
            
            int dillyDallyBeforeLunch = rng.nextInt((80 - 10) + 1) + 10;
            Thread.sleep(dillyDallyBeforeLunch);
            
            System.out.println("Developer " + this.getName() +
                " Goes to lunch at " + team.getLead().getPM().getClockTime());

            int lunchDuration = rng.nextInt((600 - 300) + 1) + 300;
            Thread.sleep(lunchDuration);
            
            System.out.println("Developer " + this.getName() +
                " Leaves lunch at " + team.getLead().getPM().getClockTime());
            
            //Go to final meeting somewhere between 4:00 and 4:15
            int workInterval = rng.nextInt((4950 - 4800) + 1) + 4800;
            working(workInterval, false);

            System.out.println("Developer " + this.getName() +
                " Goes to afternoon meeting at " + team.getLead().getPM().getClockTime());
            
            //Wait for that afternoon meeting to kick off
            team.getLead().getPM().getAfternoonMeetingBarrier().await();

            //After the meeting, get ready to go home and end the day
            int dillyDallyBeforeHome = rng.nextInt((150 - 10) + 1) + 10;
            Thread.sleep(dillyDallyBeforeHome);

            //Leave after afternoon meeting is complete
            System.out.println("Developer " + this.getName() +
                " is heading home at " + team.getLead().getPM().getClockTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void working(int endTime, boolean isLead) throws InterruptedException {
      int min = 1;
      while(team.getLead().getPM().getTime() < endTime) {

        int maxTicks = max * 10000000;

        //Do work, asking questions when necessary until lunch
        int randomQuestionChance = rng.nextInt((maxTicks - min) + 1) + min;
        if(randomQuestionChance == 1 && !isLead) {
          System.out.println("Developer " + this.getName() + " has a question!");
          team.getLead().askQuestion(this);
        }
      }
    }

    protected void arrive() throws InterruptedException {
        //Enter the office anywhere between 8 and 8:30 AM
        int arrivalMillis = rng.nextInt(290);
        Thread.sleep(arrivalMillis);

        String arrivalTime = (arrivalMillis / 10 < 10) ?
                ("0" + Integer.toString(arrivalMillis / 10)) : Integer.toString(arrivalMillis / 10);

        System.out.println("Developer " + super.getName() + " enters the office at 8:"
                + arrivalTime);
    }
}
