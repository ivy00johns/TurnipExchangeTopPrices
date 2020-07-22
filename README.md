# Welcome fans of Turnip.Exchange!!!

As an avid fan of "Animal Crossing: New Horizon" I find myself on [Turnip Exchange](https://turnip.exchange/islands) pretty much daily checking the current Turnip Prices.

Initially I would manually grab the Turnip Prices, sort them using [CyberChef](https://gchq.github.io/CyberChef/#recipe=Sort('Line%20feed',false,'Alphabetical%20(case%20sensitive)')), manually generate a Slack message and share it in the "Animal Crossing" Slack Channel at work.

As an Automation Engineer though, I knew I could do so much better, so I created the following Selenium based script that manipulates the UI and grabs the data I need.


### PLEASE NOTE
This is the ALPHA version of the script that prints out an array of the Turnip Prices ONLY. 


# SETUP
1. `mvn clean install`

2. Run the `main()` function.

3. Example output:

    *   ```
        Number of Islands: 117
        
        Turnip.Exchange prices over 600 :bells:
        -------------------------------------------
        - :ac-turnip: Price: 639 :bells:
        - :ac-turnip: Price: 629 :bells:
        - :ac-turnip: Price: 628 :bells:
        - :ac-turnip: Price: 652 :bells:
        ```


# TODO
* Retrieve additional details:

    * Link to the Island queue
    * Island details
    * Current queue
