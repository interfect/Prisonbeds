Prisonbeds
==========

Prisonbeds is a Bukkit plugin that allows tou to imprison people in Minecraft.

Prisonbeds provides the following commands:

 * /pbset - Primes a prison at your current location. Prisons stay primed until you /pbunset them. All players you kill while you have a prison primed will be imprisoned in that prison.
 * /pbunset - Unsets your primed prison, so that players you kill die normally.
 * /pbinfo - Reports if you have a prison currently primed, and, if so, where it is.
 * /pbhelp - Displays help on available Prisonbeds commands
 
Previous versions of this plugin required you to return to your prison and /pbset before every imprisonment; primed prisons did not persist across logins or server restarts. Now that you can /pbset once and imprison an arbitrary number of players, some changes have been made to balance this. Specifically, when someone is imprisoned, the location of the prison is announced to all online players, making it easier for people to immediately respond and try to free them.

To combat attempts to glitch out of prisons, prisoners are now teleported to their imprisonment location on each login. This should not affect prisoners not trying to glitch (unless the log out while trying to escape!), but prevents rapid re-logging from being used to exit through the ceiling.