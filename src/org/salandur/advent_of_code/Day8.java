package org.salandur.advent_of_code;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.util.stream.Collectors.toCollection;

public class Day8 {
    public static void main(String[] args) throws IOException {
        String licenseDataText = Files.readAllLines(Path.of("day8.txt")).get(0);

        Queue<Integer> licenseData = Arrays.stream(StringUtils.split(licenseDataText)).map(Integer::valueOf).collect(toCollection(LinkedList::new));

        LicenseNode rootNode = parseNodeFromData(licenseData);
        System.out.println(rootNode);

        System.out.printf("Day 8.1: the value of the license check: %d\n", rootNode.metadataChecksum());
    }

    private static LicenseNode parseNodeFromData(Queue<Integer> licenseData) {
        Integer numberOfChildren = licenseData.poll();
        Integer numberOfMetaData = licenseData.poll();

        LicenseNode node = new LicenseNode(numberOfChildren, numberOfMetaData);

        for (int i = 0; i < numberOfChildren; i++) {
            node.children.add(parseNodeFromData(licenseData));
        }

        for (int i = 0; i < numberOfMetaData; i++) {
            node.metaData.add(licenseData.poll());
        }

        return node;
    }

    private static class LicenseNode {
        private final List<LicenseNode> children;
        private final List<Integer> metaData;

        public LicenseNode(Integer numberOfChildren, Integer numberOfMetaData) {
            children = new ArrayList<>(numberOfChildren);
            metaData = new ArrayList<>(numberOfMetaData);
        }

        public int metadataChecksum() {
            return metaData.stream().mapToInt(Integer::intValue).sum() +
                    children.stream().mapToInt(LicenseNode::metadataChecksum).sum();
        }

    }
}
