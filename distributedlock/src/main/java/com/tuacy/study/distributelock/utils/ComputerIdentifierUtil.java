package com.tuacy.study.distributelock.utils;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class ComputerIdentifierUtil {

    /**
     * Generates a Computer Identifier, which may be part of a strategy to construct
     * a licence key. (The identifier may not be unique as in one case hashcode
     * could be same for multiple values, and the result may differ based on whether
     * the program is running with sudo/root permission.) The identifier string is
     * based upon the processor serial number, vendor, processor identifier, and
     * total processor count.
     *
     * @return A string containing four hyphen-delimited fields representing the
     * processor; the first 3 are 32-bit hexadecimal values and the last one
     * is an integer key.
     */
    public static String getComputerIdentifier() {
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
        ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();

        String vendor = operatingSystem.getManufacturer();
        String processorSerialNumber = computerSystem.getSerialNumber();
        String processorIdentifier = centralProcessor.getIdentifier();
        int processors = centralProcessor.getLogicalProcessorCount();

        String delimiter = "-";

        return String.format("%08x", vendor.hashCode()) + delimiter
                + String.format("%08x", processorSerialNumber.hashCode()) + delimiter
                + String.format("%08x", processorIdentifier.hashCode()) + delimiter + processors;
    }

}
