package net.kyc.client.impl.manager.player;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.kyc.client.Heckk;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.UpdateSelectedSlotS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import net.kyc.client.api.event.listener.EventListener;
import net.kyc.client.impl.event.network.PacketEvent;
import net.kyc.client.init.Managers;
import net.kyc.client.util.Globals;

import java.util.ArrayList;

/**
 * @author linus
 * @since 1.0
 */
public class InventoryManager implements Globals {

    // The serverside selected hotbar slot. This will determine the held item
    // serverside
    private int slot;

    /**
     *
     */
    public InventoryManager() {
        Heckk.EVENT_HANDLER.subscribe(this);
    }

    @EventListener
    public void onPacketOutBound(final PacketEvent.Outbound event) {
        if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket packet) {
            slot = packet.getSelectedSlot();
        }
    }

    @EventListener
    public void onPacketInbound(final PacketEvent.Inbound event) {
        if (event.getPacket() instanceof UpdateSelectedSlotS2CPacket packet) {
            slot = packet.getSlot();
        }
    }

    /**
     * Sets the server slot via a {@link UpdateSelectedSlotC2SPacket}
     * @param barSlot the player hotbar slot 0-8
     * @see InventoryManager#setSlotForced(int)
     * @apiNote Method will not do anything if the slot provided is already the server slot
     */
    public void setSlot(final int barSlot) {
        if (slot != barSlot && PlayerInventory.isValidHotbarIndex(barSlot)) {
            setSlotForced(barSlot);
        }
    }

    /**
     * Sets the server & client slot
     * @param barSlot the player hotbar slot 0-8
     * @apiNote Method will not do anything if the slot provided is already the server slot
     * @see InventoryManager#setSlotForced(int)
     * @see InventoryManager#setSlot(int)
     */
    public void setClientSlot(final int barSlot) {
        if (mc.player.getInventory().selectedSlot != barSlot
            && PlayerInventory.isValidHotbarIndex(barSlot)) {
            mc.player.getInventory().selectedSlot = barSlot;
            setSlotForced(barSlot);
        }
    }

    /**
     * Sends a {@link UpdateSelectedSlotC2SPacket} without any slot chekcs
     * @param barSlot the player hotbar slot 0-8
     */
    public void setSlotForced(final int barSlot) {
        Managers.NETWORK.sendPacket(new UpdateSelectedSlotC2SPacket(barSlot));
    }

    /**
     * Syncs the server slot to the client slot
     */
    public void syncToClient() {
        if (isDesynced()) {
            setSlotForced(mc.player.getInventory().selectedSlot);
        }
    }

    public boolean isDesynced()
    {
        return mc.player.getInventory().selectedSlot != slot;
    }

    //
    public void closeScreen() {
        Managers.NETWORK.sendPacket(new CloseHandledScreenC2SPacket(mc.player.currentScreenHandler.syncId));
    }

    /**
     * @param slot
     */
    public void pickupSlot(final int slot) {
        click(slot, 0, SlotActionType.PICKUP);
    }

    public void quickMove(final int slot) {
        click(slot, 0, SlotActionType.QUICK_MOVE);
    }

    /**
     * @param slot
     */
    public void throwSlot(final int slot) {
        click(slot, 0, SlotActionType.THROW);
    }

    /**
     * @param slot
     * @param button
     * @param type
     */
    private void click(int slot, int button, SlotActionType type) {
        ScreenHandler screenHandler = mc.player.currentScreenHandler;
        DefaultedList<Slot> defaultedList = screenHandler.slots;
        int i = defaultedList.size();
        ArrayList<ItemStack> list = Lists.newArrayListWithCapacity(i);
        for (Slot slot1 : defaultedList) {
            list.add(slot1.getStack().copy());
        }
        screenHandler.onSlotClick(slot, button, type, mc.player);
        Int2ObjectOpenHashMap<ItemStack> int2ObjectMap = new Int2ObjectOpenHashMap<>();
        for (int j = 0; j < i; ++j) {
            ItemStack itemStack2;
            ItemStack itemStack = list.get(j);
            if (ItemStack.areEqual(itemStack, itemStack2 = defaultedList.get(j).getStack())) continue;
            int2ObjectMap.put(j, itemStack2.copy());
        }
        mc.player.networkHandler.sendPacket(new ClickSlotC2SPacket(screenHandler.syncId, screenHandler.getRevision(), slot, button, type, screenHandler.getCursorStack().copy(), int2ObjectMap));
    }

    /**
     * @param item
     * @return
     */
    public int count(Item item) {
        ItemStack offhandStack = mc.player.getOffHandStack();
        int itemCount = offhandStack.getItem() == item ? offhandStack.getCount() : 0;
        for (int i = 0; i < 36; i++) {
            ItemStack slot = mc.player.getInventory().getStack(i);
            if (slot.getItem() == item) {
                itemCount += slot.getCount();
            }
        }
        return itemCount;
    }

    /**
     * @return
     */
    public int getServerSlot() {
        return slot;
    }

    /**
     * @return
     */
    public ItemStack getServerItem() {
        if (mc.player != null && getServerSlot() != -1) {
            return mc.player.getInventory().getStack(getServerSlot());
        }
        return null;
    }
}
