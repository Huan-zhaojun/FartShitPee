package com.huan.fart_shit_pee.common.command;

import com.huan.fart_shit_pee.fart_shit_pee;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.function.Function;

public class fsp {
    public fsp(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("fsp")
                .requires((commandSource -> commandSource.hasPermissionLevel(2)))
                .then(Commands.literal("urineLevel_Max")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                        .executes(c -> setUrineLevel_Max(c, EntityArgument.getPlayers(c, "targets"), IntegerArgumentType.getInteger(c, "value")))
                                )
                        )
                )
                .then(Commands.literal("shitLevel_Max")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("value", IntegerArgumentType.integer(1))
                                        .executes(c -> setShitLevel_Max(c, EntityArgument.getPlayers(c, "targets"), IntegerArgumentType.getInteger(c, "value")))
                                )
                        )
                )
                .then(Commands.literal("urineLevel")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                        .executes(c -> setUrineLevel(c, EntityArgument.getPlayers(c, "targets"), IntegerArgumentType.getInteger(c, "value")))
                                )
                        )
                )
                .then(Commands.literal("shitLevel")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                        .executes(c -> setShitLevel(c, EntityArgument.getPlayers(c, "targets"), IntegerArgumentType.getInteger(c, "value")))
                                )
                        )
                )
                .then(Commands.literal("flatusLevel")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("value", IntegerArgumentType.integer(0, 9))
                                        .executes(c -> setFlatusLevel(c, EntityArgument.getPlayers(c, "targets"), IntegerArgumentType.getInteger(c, "value")))
                                )
                        )
                )
                .then(Commands.literal("pee")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(c -> setPee(c, EntityArgument.getPlayers(c, "targets"), BoolArgumentType.getBool(c, "value")))
                                )
                        )
                )
                .then(Commands.literal("parabola")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("a", DoubleArgumentType.doubleArg(0.0000001, 1))
                                        .then(Commands.argument("b", DoubleArgumentType.doubleArg(0.1, 255))
                                                .executes(c -> setParabola(c, EntityArgument.getPlayers(c, "targets"), DoubleArgumentType.getDouble(c, "a"), DoubleArgumentType.getDouble(c, "b")))
                                        )
                                )
                        )
                )
        );
    }

    public static int setUrineLevel_Max(CommandContext<CommandSource> context, Collection<ServerPlayerEntity> players, int value) {
        CommandSource source = context.getSource();
        for (ServerPlayerEntity player : players) {
            player.getCapability(fart_shit_pee.Drain_Capability).ifPresent(c -> c.setUrineLevel_Max(value));
        }
        source.sendFeedback(new TranslationTextComponent("commands.fsp.setUrineLevel_Max"), true);
        return 0;
    }

    public static int setUrineLevel(CommandContext<CommandSource> context, Collection<ServerPlayerEntity> players, int value) {
        CommandSource source = context.getSource();
        for (ServerPlayerEntity player : players) {
            player.getCapability(fart_shit_pee.Drain_Capability).ifPresent(c -> c.setUrineLevel(value));
        }
        source.sendFeedback(new TranslationTextComponent("commands.fsp.setUrineLevel"), true);
        return 0;
    }

    public static int setShitLevel_Max(CommandContext<CommandSource> context, Collection<ServerPlayerEntity> players, int value) {
        CommandSource source = context.getSource();
        for (ServerPlayerEntity player : players) {
            player.getCapability(fart_shit_pee.Drain_Capability).ifPresent(c -> c.setShitLevel_Max(value));
        }
        source.sendFeedback(new TranslationTextComponent("commands.fsp.setShitLevel_Max"), true);
        return 0;
    }

    public static int setShitLevel(CommandContext<CommandSource> context, Collection<ServerPlayerEntity> players, int value) {
        CommandSource source = context.getSource();
        for (ServerPlayerEntity player : players) {
            player.getCapability(fart_shit_pee.Drain_Capability).ifPresent(c -> c.setShitLevel(value));
        }
        source.sendFeedback(new TranslationTextComponent("commands.fsp.setShitLevel"), true);
        return 0;
    }

    public static int setFlatusLevel(CommandContext<CommandSource> context, Collection<ServerPlayerEntity> players, int value) {
        CommandSource source = context.getSource();
        for (ServerPlayerEntity player : players) {
            player.getCapability(fart_shit_pee.Drain_Capability).ifPresent(c -> c.setFlatusLevel(value));
        }
        source.sendFeedback(new TranslationTextComponent("commands.fsp.setFlatusLevel"), true);
        return 0;
    }

    public static int setPee(CommandContext<CommandSource> context, Collection<ServerPlayerEntity> players, boolean value) {
        CommandSource source = context.getSource();
        for (ServerPlayerEntity player : players) {
            player.getCapability(fart_shit_pee.Drain_Capability).ifPresent(c -> c.setPee(value));
        }
        source.sendFeedback(new TranslationTextComponent("commands.fsp.setPee"), true);
        return 0;
    }

    public static int setParabola(CommandContext<CommandSource> context, Collection<ServerPlayerEntity> players, double a, double b) {
        CommandSource source = context.getSource();
        for (ServerPlayerEntity player : players) {
            player.getCapability(fart_shit_pee.Drain_Capability).ifPresent(c -> {
                c.a = a;
                c.b = b;
            });
        }
        source.sendFeedback(new TranslationTextComponent("commands.fsp.setParabola"), true);
        return 0;
    }
}
