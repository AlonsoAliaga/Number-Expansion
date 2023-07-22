# Number-Expansion
This is a [PlaceholderAPI](https://links.alonsoaliaga.com/PlaceholderAPI) expansion to allow modifying numbers a bit more.

# Installation
You can install this expansion in 2 ways:
### 1) PlaceholderAPI eCloud (Pending approval ✔️)
While being in console or having OP run the following commands:
> /papi ecloud download number\
> /papi reload

✅ Expansion is ready to be used!
### 2) Manual download
Go to [eCloud](https://api.extendedclip.com/expansions/number/) and click `Download Latest` button to get the .jar file.\
Copy and paste the file in `/plugins/PlaceholderAPI/expansions/` and run:
> /papi reload

✅ Expansion is ready to be used!
# Placeholders
The following placeholders are available:
### %number_toroman_&lt;integer&gt;%
Convert integer to roman numerals. <br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>

**Example #1:** %number_toroman_666%<br>
**Output #1:**&nbsp;`c` This is 666 in roman numerals.<br>

**Example #2:** %number_toroman_{player_level}%<br>
**Output #2:**&nbsp;`LV` This is 55 in roman numerals.

### %number_fromroman_&lt;roman-numerals&gt;%
Convert roman numerals to integer. <br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>

**Example #1:** %number_fromroman_DCLXVI%<br>
**Output #1:**&nbsp;`666` This is DCLXVI in integers.<br>

**Example #2:** %number_fromroman_{a_placeholder_in_roman_numerals}%<br>
**Output #2:**&nbsp;`12` This is the value of the placeholder (XII) in integers.

### %number_tohex_&lt;integer&gt;%
Convert integer to hex notation. <br>
Useful for menu makers where you need converting int to hex. <br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>

**Example #1:** %number_tohex_16435930%<br>
**Output #1:**&nbsp;`#FACADA` The `#` is added because of format specified.<br>

**Example #2:** %number_tohex_{an_integer_placeholder}%<br>
**Output #2:**&nbsp;`#FACADA` The `#` is added because of format specified.

### %number_fromhex_&lt;hex&gt;%
Convert hex to integer. <br>
Useful for menu makers where you need converting hex to int. <br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>

**Example #1:** %number_fromhex_FACADA%<br>
**Output #1:**&nbsp;`16435930` This is `#FACADA` as integer.<br>

**Example #2:** %number_fromhex_{a_hex_placeholder}%<br>
**Output #2:**&nbsp;`16435930` This is the value of the placeholder (#FACADA) as integers.

### %number_random_&lt;min&gt;_&lt;max&gt;%
Generate a random number between min and max (inclusive by default). <br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>

**Example #1:** %number_random_1_10%<br>
**Output #1:**&nbsp;`7` This is a random number between 1 and 10 (inclusive by default).<br>

**Example #2:** %number_random_0_{player_level}%<br>
**Output #2:**&nbsp;`21` This is a random number between 0 and player level (inclusive by default).

### %number_lastrandom_{NUMBER_OR_PLACEHOLDER_IF_NOT_PREVIOUS_RANDOM}%
Returns the last random number generated, if not available returns the provided text. <br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>

**Example:** %number_lastrandom_0%<br>
**Output:**&nbsp;`0` Returns last random number or provided text if not available.<br>

### %number_randomdouble_&lt;min&gt;_&lt;max&gt;\_\[decimals]%
Generate a random double between min and max (inclusive by default). <br>
The 2rd argument is optional `[decimals]` and is the amount of decimals. (0 by default) <br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>

**Example #1:** %number_randomdouble_1_10%<br>
**Output #1:**&nbsp;`5.0` Random number between 1 and 10 with 0 decimals (.0 is still displayed).<br>

**Example #2:** %number_randomdouble_0_{player_level}_2%<br>
**Output #2:**&nbsp;`13.57` Random number between 0 and player level with 2 decimals.

### %number_lastdoublerandom_{NUMBER_OR_PLACEHOLDER_IF_NOT_PREVIOUS_RANDOM}%
Returns the last random double generated, if not available returns the provided text. <br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>

**Example:** %number_lastdoublerandom_0.0%<br>
**Output:**&nbsp;`0.0` Returns last random double or provided text if not available.<br>

### %number_format_&lt;format-identifier&gt;_&lt;value-to-manipulate&gt;%
This allows you to use previously created custom format to format your numbers. <br>
By default it comes with many useful formats to use:<br>
`round-int` = Round provided value to the closest integer. (No decimals) <br>
`round-two-decimals` = Round provided value to a two-decimals double. <br>
`round-force-two-decimals` = Round provided value to at least two-decimals double. <br>
`thousand-separator-two-decimals` = Adds commas each 1,000. <br>
`dollar-two-decimals` = Adds commas each 1,000 and $ at the beginning. <br>
`percentage-two-decimals` = Convert doubles like 0.455 to percentage like 45.5%. <br>
You can create as many as you need in `plugins/PlaceholderAPI/config.yml` in `number-formatter` section<br>
Supports PlaceholderAPI but requires `{ }` instead of `% %`.<br>

**Example #1:** %number_format_round-int_{player_health}%<br>
**Output #1:**&nbsp;`17` Instead of returning the usual `17.35563693494242`.<br>

**Example #2:** %number_format_round-two-decimals_{player_health}%<br>
**Output #2:**&nbsp;`17.36` Instead of returning the usual `17.35563693494242`.<br>

**Example #3:** %number_round-thousand-separator-two-decimals_{vault_eco_balance}%<br>
**Output #3:**&nbsp;`2,546,785` Instead of returning the usual `2546785`.<br>

**Example #4:** %number_round-dollar-two-decimals_{vault_eco_balance}%<br>
**Output #4:**&nbsp;`$2,546,785` Instead of returning the usual `2546785`.<br>

# Want more cool and useful expansions?
<p align="center">
    <a href="https://alonsoaliaga.com/moregradients">MoreGradients Expansion</a><br>
    Generate cool hex gradients with amazing formats for scoreboard/motd/items and more!<br>
    <br>
    <a href="https://alonsoaliaga.com/translatefont">TranslateFont Expansion</a><br>
    Translate your text and make them look fancy! ᴍᴀɴʏ ғᴏɴᴛs ᴀᴠᴀɪʟᴀʙʟᴇ!<br>
    <br>
    <a href="https://alonsoaliaga.com/tempdata">TempData Expansion</a><br>
    Store in cache temporary global or per player data to use where you need!<br>
    <br>
    <a href="https://alonsoaliaga.com/capitalize">Capitalize Expansion</a><br>
    Customize texts a bit more removing underscores, dashes and capitalizing letters!<br>
    <br>
    <a href="https://alonsoaliaga.com/playerutils">PlayerUtils Expansion</a><br>
    Access to detailed information of your players like health/food/armor bar and more!<br>
    <br>
    <a href="https://alonsoaliaga.com/checkmoney">CheckMoney Expansion</a><br>
    Check if player has enough funds or not with custom output! (Specially for menu plugins)<br>
    <br>
    <a href="https://alonsoaliaga.com/checkdate">CheckDate Expansion</a><br>
    Check if server/machine date is the desired one with custom output! (Specially for messages)<br>
</p>

# Want more tools?
**Make sure to check our [BRAND NEW TOOL](https://alonsoaliaga.com/hex) to generate your own text with gradients!**<br>
<p align="center">
    <a href="https://alonsoaliaga.com/hex"><img src="https://i.imgur.com/766Es8I.png" alt="Our brand new tool!"/></a>
</p>

# Do you have a cool expansion idea?
<p align="center">
    <a href="https://alonsoaliaga.com/discord">Join us on Discord</a><br>
    <a href="https://alonsoaliaga.com/discord"><img src="https://i.imgur.com/2pslxIN.png"></a><br>
    Let us know what's your idea and it might become true!
</p>

# Questions?
<p align="center">
    <a href="https://alonsoaliaga.com/discord"><img style="width:200px;" src="https://i.imgur.com/laEFHcG.gif"></a><br>
    <a href="https://alonsoaliaga.com/discord"><span style="font-size:28px;font-weight:bold;color:rgb(100,100,255);">Join us in our discord!</span></a>
</p>