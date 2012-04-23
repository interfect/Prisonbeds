Prisonbeds
==========

Prisonbeds is a Bukkit plugin that allows tou to imprison people in Minecraft.

Prisonbeds provides the following commands:

 * /pbset - Primes a prison at your current location. Prisons stay primed until you /pbunset them or use them. A player you kill while you have a prison primed will be imprisoned in that prison.
 * /pbunset - Unsets your primed prison, so that players you kill die normally.
 * /pbinfo - Reports if you have a prison currently primed, and, if so, where it is.
 * /pbhelp - Displays help on available Prisonbeds commands
 
Previous versions of this plugin required you to return to your prison and /pbset on every login;
primed prisons did not persist across logins or server restarts.

When someone is imprisoned, the location of the prison is now announced to all online players,
making it easier for people to immediately respond and try to free them.

To combat attempts to glitch out of prisons, prisoners are now teleported to their imprisonment
location on each login. This should not affect prisoners not trying to glitch (unless they log out
while trying to escape!), but prevents rapid re-logging from being used to exit through the ceiling.