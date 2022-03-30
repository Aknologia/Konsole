package dev.aknologia.konsole.command;

import dev.aknologia.konsole.KonsoleClient;
import dev.aknologia.konsole.interfaces.MinecraftClientMixinInterface;
import dev.aknologia.konsole.niflheim.Command;
import dev.aknologia.konsole.niflheim.CommandDispatcher;
import dev.aknologia.konsole.niflheim.arguments.Argument;
import dev.aknologia.konsole.niflheim.arguments.StringArgumentType;
import dev.aknologia.konsole.niflheim.context.CommandContext;
import dev.aknologia.konsole.niflheim.exceptions.CommandSyntaxException;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ActionCommands {
    public void register(CommandDispatcher dispatcher) {
        new AttackCommand().register(dispatcher);
        new AttackLoopCommand().register(dispatcher);
        new UseCommand().register(dispatcher);
        new UseLoopCommand().register(dispatcher);
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
            ((MinecraftClientMixinInterface) MinecraftClient.getInstance()).doAttackMixed();
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

    class AttackLoopCommand implements Command {
        public String name = "*attack";
        public String description = "Attack with the main hand continuously.";
        public List<Argument> arguments = new ArrayList<>();

        private boolean enabled = false;

        @Override
        public void register(CommandDispatcher dispatcher) {
            dispatcher.register(this);
        }

        @Override
        public int run(CommandContext context) throws CommandSyntaxException {
            this.enabled = !this.enabled;
            if(this.enabled) this.trigger();
            return 1;
        }

        private void trigger() {
            AttackLoopCommand instance = this;
            Timer t = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if(!instance.enabled) t.cancel();
                    else ((MinecraftClientMixinInterface) KonsoleClient.CLIENT).doAttackOrCooldown();
                }
            };
            t.scheduleAtFixedRate(task, new Date(), 50);
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

    class UseCommand implements Command {
        public String name = "+use";
        public String description = "Use the selected item or block.";
        public List<Argument> arguments = new ArrayList<>();

        @Override
        public void register(CommandDispatcher dispatcher) {
            dispatcher.register(this);
        }

        @Override
        public int run(CommandContext context) throws CommandSyntaxException {
            ((MinecraftClientMixinInterface) KonsoleClient.CLIENT).doItemUseMixed();
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

    class UseLoopCommand implements Command {
        public String name = "*use";
        public String description = "Use the selected item or block continuously.";
        public List<Argument> arguments = new ArrayList<>();

        private boolean enabled = false;

        @Override
        public void register(CommandDispatcher dispatcher) {
            dispatcher.register(this);
        }

        @Override
        public int run(CommandContext context) throws CommandSyntaxException {
            this.enabled = !this.enabled;
            if(this.enabled) this.trigger();
            return 1;
        }

        private void trigger() {
            UseLoopCommand instance = this;
            Timer t = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if(!instance.enabled) t.cancel();
                    else ((MinecraftClientMixinInterface) KonsoleClient.CLIENT).doItemUseMixed();
                }
            };
            t.scheduleAtFixedRate(task, new Date(), 50);
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
            KonsoleClient.CLIENT.player.input.jumping = true;
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
