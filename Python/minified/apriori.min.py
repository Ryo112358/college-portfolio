'''
Apriori Algorithm in Python3 minified
 - Sample input and output files included in root
 - Usage: python apriori.min.py <in.csv> <out.csv> <min_support_percentage> <min_rule_confidence>
 - Example: python apriori.min.py input.csv output.csv 0.50 0.75
@author: Ryo112358
'''
import sys
import csv
import math
import itertools
print()
def printDeepList(anyList):
    for index in anyList:
        print(index);
def nCr(n,r):
    f = math.factorial
    return f(n) // f(r) // f(n-r);
def totalCombinations(setSize):
    total = 0;
    for i in range(setSize):
        total += nCr(setSize, i)
    return total;
def removeZeroIndexOfList(anyList):
    newList = []
    for i, row in enumerate(anyList):
        if i > 0:
            newList.append(anyList[i])
    return newList;
def defineSingleElements(transactions):
    uniqueElements = []
    contains = False
    for i, row in enumerate(transactions):
        for tcell in transactions[i]:
            for element in uniqueElements:
                if element == tcell:
                    contains = True
                    break
            if not contains:
                uniqueElements.append(tcell)
            contains = False
    uniqueElements.sort()
    return uniqueElements;
def comboFreq(transactions, combination):
    count = 0;
    for row in transactions:
        if combination.issubset(row):
            count += 1
    return count;
def findAllSubsets(anySet):
    subsets = []
    for i in range(1, len(anySet)+1):
        iSizedSets = [set(x) for x in itertools.combinations(anySet, i)]
        subsets.append(iSizedSets)
    return subsets;
def returnSubsetIndex(listOfSets, subset):
    return listOfSets.index(subset);
def returnAllSubsetSupPercentages(listOfSubsets, combos, percentages):
    subset_perc = []
    for row in listOfSubsets:
        j_list = []
        for sizeIndex in row:
            k_list = []
            for subset in sizeIndex:
                k_list.append(percentages[returnSubsetIndex(combos, subset)])
            j_list.append(k_list)
        subset_perc.append(j_list)
    return subset_perc;
def calculateConfidences(percList):
    subset_confidence = percList
    for i, row in enumerate(percList):
        for j, sizeIndex in enumerate(row):
            length = len(sizeIndex)
            if length == 1:
                break
            for k, subset in enumerate(sizeIndex):
                subset_confidence[i][j][k] = round(subset_confidence[i][length-1][0]/subset_confidence[i][j][k], 6)
    return subset_confidence;
def writeToFile(anyString):
    with open(output_filename, "a") as myfile:
        myfile.write(anyString + '\n');
def printSets(setList, supList):
    for i, eachSet in enumerate(setList):
        setElements = ''
        while len(eachSet) != 0:
            setElements += ',' + str(eachSet.pop())
        sup_count = "{:.6f}".format(supList[i])
        formattedSet = ('set,' + sup_count + setElements)
        print(formattedSet)
        writeToFile(formattedSet);
def printRules(subsets, confidences, min_confidence):
    for i, row in enumerate(subsets):
        for j, sizeIndex in enumerate(row):
            length = len(sizeIndex)
            if length == 1:
                break
            for k, subset in enumerate(sizeIndex):
                wholeSubset = set()
                if confidences[i][j][k] >= min_confidence:
                    deleteElements = set()
                    leftElements = ''
                    while len(subset) != 0:
                        element = subset.pop()
                        leftElements += str(element) + ','
                        deleteElements.add(element)
                    for e in subsets[i][length-1][0]:
                        wholeSubset.add(e)
                    wholeSubset.difference_update(deleteElements)
                    rightElements = ''
                    while len(wholeSubset) != 0:
                        rightElements += ',' + str(wholeSubset.pop())
                    sup_count = "{:.6f}".format(confidences[i][length-1][0])
                    conf = "{:.6f}".format(confidences[i][j][k])
                    formattedRule = ('rule,' + sup_count + ',' + conf + ',' + leftElements + "'=>'" + rightElements)
                    print(formattedRule)
                    writeToFile(formattedRule);
input_filename = sys.argv[1]
output_filename = sys.argv[2]
min_support_percentage = float(sys.argv[3])
min_confidence = float(sys.argv[4])
num_transactions = 0
num_unique_elements = 0
num_combinations = 0
dataAsSets = list(csv.reader(open(input_filename)))
num_transactions = len(dataAsSets)
for i, row in enumerate(dataAsSets):
    dataAsSets[i] = set(removeZeroIndexOfList(dataAsSets[i]))
elements = defineSingleElements(dataAsSets)
num_unique_elements = len(elements)
num_combinations = totalCombinations(num_unique_elements)
filtered_combs = []
filtered_sup_percentage = []
for i in range(1, len(elements)+1):
    iSizedCombos = [set(x) for x in itertools.combinations(elements, i)]
    for combination in iSizedCombos:
        comboSupCount = int(comboFreq(dataAsSets, combination))
        comboSupPerc = comboSupCount/num_transactions
        if comboSupPerc >= min_support_percentage:
            filtered_combs.append(combination)
            filtered_sup_percentage.append(comboSupPerc)
all_subsets = []
for combo in filtered_combs:
    all_subsets.append(findAllSubsets(combo))
subsets_sup_percentages = returnAllSubsetSupPercentages(all_subsets, filtered_combs, filtered_sup_percentage)
confidences = calculateConfidences(subsets_sup_percentages)
open(output_filename, "w")
printSets(filtered_combs, filtered_sup_percentage)
printRules(all_subsets, confidences, min_confidence)
