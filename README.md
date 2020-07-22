# Welcome fans of Turnip.Exchange!!!

As an avid fan of "Animal Crossing: New Horizon" I find myself on [Turnip Exchange](https://turnip.exchange/islands) pretty much daily checking the current Turnip Prices.

Initially I would manually grab the Turnip Prices, sort them using [CyberChef](https://gchq.github.io/CyberChef/#recipe=Sort('Line%20feed',false,'Alphabetical%20(case%20sensitive)')), manually generate a Slack message and share it in the "Animal Crossing" Slack Channel at work.

As an Automation Engineer though, I knew I could do so much better, so I created the following Selenium based script that manipulates the UI and grabs the data I need.


### PLEASE NOTE
* This is the **ALPHA version** of the script. More details will be added.
* You will need [Google Chrome](https://www.google.com/chrome/) `v81+`.


# SETUP
1. `mvn clean install`

2. Run the `main()` function.

3. Example output:
    ```
     Number of Islands: 111

     **Turnip.Exchange** prices over 600 :bells:
     -------------------------------------------
     - :ac-turnip: price: 649 :bells:
     - :ac-turnip: price: 620 :bells:
     -------------------------------------------
     Time: 2020-07-22 16:18:35.374
     ```

# CUSTOMIZATION
You can customize the Turnip Price threshold and Slack Emojis that are used.

 1. Open `src/main/java/GenerateTurnipExchangeTopPrices.java`.

 2. Update the following variables:
     * `MIN_TURNIP_PRICE`
     * `TURNIP_SLACK_EMOJI_ALIAS`
     * `BELLS_SLACK_EMOJI_ALIAS`

# TODO
* Retrieve additional details:

    * Link to the Island queue
    * Island details
    * Current queue
