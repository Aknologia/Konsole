package dev.aknologia.konsole.command;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;

public class ActionCommands {
    public void register(CommandDispatcher dispatcher) {
        new AttackCommand().register(dispatcher);
        new Attack2Command().register(dispatcher);
        new JumpCommand().register(dispatcher);
        new SneakCommand().register(dispatcher);
        new UnsneakCommand().register(dispatcher);
    }

    class AttackCommand implements Command {
        public String name = "+attack";
        public String description = "Attack with the main hand.";
        public List<Argument> arguments = new ArrayList<>();

        @Override
        public void register(CommandDispatcher dispatcher) {
            dispatcher.register(this);
        }

        @Override
        public int run(CommandContext context) throws CommandSyntaxException {
            KonsoleClient.CLIENT.player.swingHand(Hand.MAIN_HAND);
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
    }

    class Attack2Command implements Command {
        public String name = "+attack2";
        public String description = "Attack with the off hand.";
        public List<Argument> arguments = new ArrayList<>();

        @Override
        public void register(CommandDispatcher dispatcher) {
            dispatcher.register(this);
        }

        @Override
        public int run(CommandContext context) throws CommandSyntaxException {
            KonsoleClient.CLIENT.player.swingHand(Hand.OFF_HAND);
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
    }

    class JumpCommand implements Command {
        public String name = "+jump";
        public String description = "Jump once.";
        public List<Argument> arguments = new ArrayList<>();

        @Override
        public void register(CommandDispatcher dispatcher) {
            dispatcher.register(this);
        }

        @Override
        public int run(CommandContext context) throws CommandSyntaxException {
            KonsoleClient.CLIENT.player.jump();
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
    }

    class SneakCommand implements Command {
        public String name = "+sneak";
        public String description = "Enable sneaking.";
        public List<Argument> arguments = new ArrayList<>();

        @Override
        public void register(CommandDispatcher dispatcher) {
            dispatcher.register(this);
        }

        @Override
        public int run(CommandContext context) throws CommandSyntaxException {
            KonsoleClient.CLIENT.player.setSneaking(true);
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
    }

    class UnsneakCommand implements Command {
        public String name = "-sneak";
        public String description = "Disable sneaking.";
        public List<Argument> arguments = new ArrayList<>();

        @Override
        public void register(CommandDispatcher dispatcher) {
            dispatcher.register(this);
        }

        @Override
        public int run(CommandContext context) throws CommandSyntaxException {
            KonsoleClient.CLIENT.player.setSneaking(false);
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
    }
}
