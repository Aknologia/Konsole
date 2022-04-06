<div>
  <a href="#"><img width="230" height="230" align="left" src="https://raw.githubusercontent.com/Aknologia/Konsole/master/src/main/resources/assets/konsole/icon.png" alt="Konsole"/></a>
  <br>
  <h1>Konsole 
    <a href="https://github.com/Aknologia/Konsole/actions/workflows/build.yml"><img
            src="https://github.com/Aknologia/Konsole/actions/workflows/build.yml/badge.svg" alt="Java CI"/></a>
    <a href="https://github.com/Aknologia/Konsole/releases"><img
            src="https://img.shields.io/github/v/release/Aknologia/Konsole?include_prereleases"/></a>
  </h1>
  <p>Konsole is a Minecraft Clientside mod that adds a brand-new console to the game. It is mainly used for debugging and utility purposes.</p>
  <h6>MIT © 2022 Aknologia</h6>
  <a href="https://www.minecraft.net/en-us/download"><img
          src="https://img.shields.io/badge/minecraft-1.18.2-yellowgreen"/></a>
  <a href="https://fabricmc.net/use/installer/"><img
          src="https://img.shields.io/badge/fabric%20loader-%5E0.13.3-fffca3"/></a>
  <a href="https://www.curseforge.com/minecraft/mc-mods/fabric-api"><img
          src="https://img.shields.io/badge/fabric%20api-0.48.0%2B1.18.2-ffe3a3"/></a>
  <br>
  <br>
  <p align="center">
      <a href="https://sonarcloud.io/summary/new_code?id=Aknologia_Konsole"><img
            src="https://sonarcloud.io/api/project_badges/measure?project=Aknologia_Konsole&metric=sqale_rating"/></a>
      <a href="https://sonarcloud.io/summary/new_code?id=Aknologia_Konsole"><img
            src="https://sonarcloud.io/api/project_badges/measure?project=Aknologia_Konsole&metric=reliability_rating"/></a>
      <a href="https://sonarcloud.io/summary/new_code?id=Aknologia_Konsole"><img
            src="https://sonarcloud.io/api/project_badges/measure?project=Aknologia_Konsole&metric=security_rating"/></a>
      <br>
      <a href="https://sonarcloud.io/summary/new_code?id=Aknologia_Konsole"><img
            src="https://sonarcloud.io/api/project_badges/measure?project=Aknologia_Konsole&metric=ncloc"/></a>
      <a href="https://sonarcloud.io/summary/new_code?id=Aknologia_Konsole"><img
            src="https://sonarcloud.io/api/project_badges/measure?project=Aknologia_Konsole&metric=sqale_index"/></a>
      <a href="https://sonarcloud.io/summary/new_code?id=Aknologia_Konsole"><img
            src="https://sonarcloud.io/api/project_badges/measure?project=Aknologia_Konsole&metric=code_smells"/></a>
      <a href="https://sonarcloud.io/summary/new_code?id=Aknologia_Konsole"><img
              src="https://sonarcloud.io/api/project_badges/measure?project=Aknologia_Konsole&metric=bugs"/></a>
  </p>
</div>

## Commands
### Action
| Name | Usage | Description |
| ------ | ------- | ------------- |
| +attack | `+attack` | *Attack with the main hand.* |
| *attack | `*attack` | *Attack with the main hand continuously. (Waits for weapon cooldowns)* |
| +jump | `+jump` | *Jump once.* |
| *jump | `*jump` | *Jump continuously.* |
| +pick | `+pick` | *Pick an item.* |
| *pick | `*pick` | *Pick items continuously.* |
| +sneak | `+sneak` | *Toggle sneaking.* |
| +use | `+use` | *Use the selected item or block.* |
| *use | `*use` | *Use the selected item or block continuously.* |
### Info
| Name | Usage | Description |
| ------ | ------- | ------------- |
| ping | `ping` | *Show your current latency in milliseconds.* |
| players | `players` | *Show a list of connected players and their latency.* |
| tps | `tps` | *Show the integrated server's tps and packets. (Singleplayer only)* |
| whois | `whois <player:Player>` | *Show all available information about the specified player.* |
| near | `near [box_radius:Integer:min=1,default=50]` | *Show all living entities in the specified box radius.* |
| world | `world` | *Show all available information about the current world.* |
| dimension | `dimension` | *Show all available information about the current dimension.* |
| pos | `pos` | *Show all available information about the current position, including the targeted block and fluid.* |
| distance | `distance <x1:Double> <y1:Double> <z1:Double> [x2:Double] [y2:Double] [z2:Double]` | *Calculates the distance between 2 positions.* |
### Utility
| Name | Usage | Description |
| ------ | ------- | ------------- |
| help | `help` | *Show a list of all available commands.* |
| echo | `echo  <message:GreedyString>` | *Send a message in the console.* |
| clear | `clear` | *Clear all messages in the console.* |
| disconnect | `disconnect` | *Leave your current server/integrated server.* |
| quit | `quit` | *Close the game.* |
| refresh | `refresh` | *Refresh all loaded chunks.* |
| say | `say <message:GreedyString>` | *Send a message in the chat. (Allows slash commands)* |
| bind | `bind <key:Key> <command:QuotedString>` | *Bind the specified command to the specified key.* |
| unbind | `unbind <key:Key>` | *Remove the current bind on the specified key.* |
| unbindall | `unbindall` | *Remove all binds. * |

## Keybinds
###### Open Console : <kbd>Right Shift</kbd>
