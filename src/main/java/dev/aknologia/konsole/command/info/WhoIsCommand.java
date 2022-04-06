package dev.aknologia.konsole.command.info;

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.command.InfoCategory;
import dev.aknologia.konsole.niflheim.Category;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.PlayerArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.LiteralText;

import java.util.*;

public class WhoIsCommand implements Command {
    public String name = "whois";
    public String description = "Show all available information of the specified player.";
    public Class<?> category = InfoCategory.class;
    public List<Argument> arguments = List.of(
            new Argument("player", PlayerArgumentType.player(), true)
    );

    @Override
    public int run(CommandContext context) throws CommandSyntaxException {
        PlayerListEntry player = PlayerArgumentType.getPlayer(context, "player");

        // LOAD PROPERTIES
        PropertyMap properties = player.getProfile().getProperties();
        List<String> stringProperties = new ArrayList<>();
        properties.keySet().stream().toList().forEach(k -> {
            Collection<Property> propertiesCollection = properties.get(k);
            Iterator<Property> iterator = propertiesCollection.iterator();
            while(iterator.hasNext()) {
                Property property = iterator.next();
                stringProperties.add(String.format("- %s:\u00A77 %s", property.getName(), property.getValue().length() > 20 ? property.getValue().substring(0, 17) + "..." : property.getValue()));
            }
        });

        // FORMAT
        String profile = String.format("""
\u00A7l%s \u00A7r\u00A77\u00A7o%sms
\u00A76\u00A7nHealth:\u00A7r %s/20

\u00A76\u00A7nGameMode:\u00A7r %s %s

\u00A76\u00A7nModel:\u00A7r %s
\u00A76\u00A7nSkin Texture:\u00A7r %s %s

\u00A77\u00A7oUUID: %s
""",
                player.getDisplayName() != null ? player.getDisplayName().getString() : player.getProfile().getName(),
                player.getLatency(),
                player.getLastHealth(),
                player.getGameMode().getName(),
                player.getScoreboardTeam() != null ? "\n\u00A76\u00A7nTeam:\u00A7r " + player.getScoreboardTeam().getDisplayName().getString() : "",
                player.getModel(),
                player.getSkinTexture().toString(),
                stringProperties.size() > 0 ? "\n\n\u00A76\u00A7nProperties:\u00A7r\n" + String.join("\n", stringProperties) : "",
                player.getProfile().getId());

        KonsoleClient.KONSOLE.addMessage(new LiteralText(profile));
        return 1;
    }

    @Override
    public List<Argument> getArguments() {
        return this.arguments;
    }

    @Override
    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Class<Category> getCategory() { return (Class<Category>) this.category; }
}
