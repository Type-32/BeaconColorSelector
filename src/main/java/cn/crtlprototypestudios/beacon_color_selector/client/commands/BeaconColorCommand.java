package cn.crtlprototypestudios.beacon_color_selector.client.commands;

import cn.crtlprototypestudios.beacon_color_selector.client.utils.BeaconColorCalculator;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BeaconColorCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("beaconcolor")
                .then(ClientCommandManager.literal("hex")
                        .then(ClientCommandManager.argument("hexCode", StringArgumentType.string())
                                .executes(BeaconColorCommand::executeHexCommand)))
                .then(ClientCommandManager.literal("blocks")
                        .then(ClientCommandManager.argument("blocks", StringArgumentType.greedyString())
                                .suggests((context, builder) -> {
                                    List<String> stainedBlocks = Registries.BLOCK.stream()
                                            .filter(block -> block.getTranslationKey().contains("stained_glass"))
                                            .map(block -> Registries.BLOCK.getId(block).toString())
                                            .collect(Collectors.toList());
                                    return CommandSource.suggestMatching(stainedBlocks, builder);
                                })
                                .executes(BeaconColorCommand::executeBlocksCommand))));
    }

    private static int executeHexCommand(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String hexCode = context.getArgument("hexCode", String.class);
        String sequence = BeaconColorCalculator.calculateStainedGlassSequence(hexCode);

        Text message = Text.literal("[Beacon Colors Sequence]").formatted(Formatting.GOLD).append("\n");
        String[] stainedBlocks = sequence.split(" ");
        for (int i = 0; i < stainedBlocks.length; i++) {
            ((MutableText) message).append(Text.literal((i + 1) + ". " + stainedBlocks[i]).formatted(Formatting.WHITE)).append("\n");
        }

        context.getSource().sendFeedback(message);
        return 1;
    }

    private static int executeBlocksCommand(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String[] blockNames = context.getArgument("blocks", String.class).split(" ");
        Color resultingColor = calculateResultingColor(blockNames);

        String hexCode = String.format("#%02x%02x%02x", resultingColor.getRed(), resultingColor.getGreen(), resultingColor.getBlue());
        Text message = Text.literal("[Beacon Colors] ").formatted(Formatting.GOLD)
                .append(Text.literal(hexCode).formatted(Formatting.WHITE));

        context.getSource().sendFeedback(message);
        return 1;
    }

    private static Color calculateResultingColor(String[] blockNames) {
        Color currentColor = new Color(255, 255, 255); // Start with white (default color)

        for (int i = 0; i < blockNames.length; i++) {
            Block block = Registries.BLOCK.get(new Identifier(blockNames[i]));
            Color blockColor = getBlockColor(block);
            currentColor = blendColors(currentColor, blockColor, i + 1);
        }

        return currentColor;
    }

    private static Color blendColors(Color currentColor, Color newColor, int step) {
        double ratio = 1.0 / (Math.pow(2, step));
        int red = (int) (currentColor.getRed() * (1 - ratio) + newColor.getRed() * ratio);
        int green = (int) (currentColor.getGreen() * (1 - ratio) + newColor.getGreen() * ratio);
        int blue = (int) (currentColor.getBlue() * (1 - ratio) + newColor.getBlue() * ratio);
        return new Color(red, green, blue);
    }

    private static Color getBlockColor(Block block) {
        if (block == Blocks.WHITE_STAINED_GLASS || block == Blocks.WHITE_STAINED_GLASS_PANE) return new Color(255, 255, 255);
        if (block == Blocks.ORANGE_STAINED_GLASS || block == Blocks.ORANGE_STAINED_GLASS_PANE) return new Color(255, 165, 0);
        if (block == Blocks.MAGENTA_STAINED_GLASS || block == Blocks.MAGENTA_STAINED_GLASS_PANE) return new Color(255, 0, 255);
        if (block == Blocks.LIGHT_BLUE_STAINED_GLASS || block == Blocks.LIGHT_BLUE_STAINED_GLASS_PANE) return new Color(173, 216, 230);
        if (block == Blocks.YELLOW_STAINED_GLASS || block == Blocks.YELLOW_STAINED_GLASS_PANE) return new Color(255, 255, 0);
        if (block == Blocks.LIME_STAINED_GLASS || block == Blocks.LIME_STAINED_GLASS_PANE) return new Color(0, 255, 0);
        if (block == Blocks.PINK_STAINED_GLASS || block == Blocks.PINK_STAINED_GLASS_PANE) return new Color(255, 192, 203);
        if (block == Blocks.GRAY_STAINED_GLASS || block == Blocks.GRAY_STAINED_GLASS_PANE) return new Color(128, 128, 128);
        if (block == Blocks.LIGHT_GRAY_STAINED_GLASS || block == Blocks.LIGHT_GRAY_STAINED_GLASS_PANE) return new Color(211, 211, 211);
        if (block == Blocks.CYAN_STAINED_GLASS || block == Blocks.CYAN_STAINED_GLASS_PANE) return new Color(0, 255, 255);
        if (block == Blocks.PURPLE_STAINED_GLASS || block == Blocks.PURPLE_STAINED_GLASS_PANE) return new Color(128, 0, 128);
        if (block == Blocks.BLUE_STAINED_GLASS || block == Blocks.BLUE_STAINED_GLASS_PANE) return new Color(0, 0, 255);
        if (block == Blocks.BROWN_STAINED_GLASS || block == Blocks.BROWN_STAINED_GLASS_PANE) return new Color(139, 69, 19);
        if (block == Blocks.GREEN_STAINED_GLASS || block == Blocks.GREEN_STAINED_GLASS_PANE) return new Color(0, 128, 0);
        if (block == Blocks.RED_STAINED_GLASS || block == Blocks.RED_STAINED_GLASS_PANE) return new Color(255, 0, 0);
        if (block == Blocks.BLACK_STAINED_GLASS || block == Blocks.BLACK_STAINED_GLASS_PANE) return new Color(0, 0, 0);
        return new Color(255, 255, 255);
    }
}