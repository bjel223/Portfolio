//The goal of EllisVigenere.cpp is to break vigenere ciphers with keylengths of 6 or less.
//It will first find the most probably key length, then attempt to use frequency analysis to solve for the key
//Finally, using the key, it will decrypt the ciphertext and print the plaintext.
//Programmed by: Benjamin Ellis
//Date finished: 03/25/2020
#include <iostream>
#include <string>
#include <vector>

const int MAXKEY = 6;
const int ALPHABET = 26;
const char ENGFREQUENT = 's';
const double EXPECTEDFREQUENCIES[26] = { 0.082,0.015,0.028,0.043,0.127,0.022,0.020,0.061,0.070,0.002,0.008,0.040,0.024,0.067,0.075,0.019,0.001,0.060,0.063,0.091,0.028,0.010,0.023,0.001,0.020,0.001 };

using namespace std;

//getCipherText simply allows users to input the ciphertext.
string getCipherText() {
    string cipherText;
    cin >> cipherText;
    return cipherText;
}

//shiftCipherText helps determineKeyLength by shifting the ciphertext string by the shiftby value.
string shiftCipherText(string cipherText, int shiftBy) {
    string shiftedText = cipherText;
    int textLength = shiftedText.length();
    for (int i = 0; i < textLength; i++) {
        shiftedText[i] = cipherText[(i + shiftBy) % textLength];
    }
    return shiftedText;
}

//Once the ciphertext has been shifted, we look for overlapping pairs in the two strings
int findOverlap(string shiftedCipherText, string cipherText) {
    int overlap = 0;
    for (int i = 0; i < (int)(shiftedCipherText.length()-1); i++) {
        if (shiftedCipherText[i] == cipherText[i] && shiftedCipherText[i + 1] == cipherText[i + 1]) {
            overlap++;
        }
    }
    return overlap;
}

//To determine the key, we shift the ciphertext by 1-6 and find which shift has the most overlapping pairs. 
int determineKeyLength(string cipherText) {
    int previousOverlap = 0;
    int overlap = 0;
    int keyLength = 0;
    string shiftedCipherText;
    for (int i = 1; i <= MAXKEY; i++) {
        shiftedCipherText = shiftCipherText(cipherText, i);
        overlap = findOverlap(shiftedCipherText, cipherText);
        if (overlap > previousOverlap) {
            previousOverlap = overlap;
            keyLength = i;
        }

    }
    return keyLength;
}

//frequencyAnalysis attempts to minimize deviation from expected frequencies to find the most likely key.
char frequencyAnalysis(string cipherText) {
    char leastDeviant;
    char keys[26] = { 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
    double currentDeviation = 0;
    //For each possible key find the variation from expected frequencies and minimize it to find the correct key.
    for (int j = 0; j < ALPHABET; j++) {
        string tempCipher = cipherText;
        int letterCount[ALPHABET] = { 0 };
        double deviation = 0;
        double frequencies[ALPHABET] = { 0 };
        for (int i = 0; i < tempCipher.length(); i++) {
            tempCipher[i] -= keys[j];
            tempCipher[i] = tempCipher[i] % ALPHABET;
            if (tempCipher[i] < 0) {
                tempCipher[i] += ALPHABET;
            }
            tempCipher[i] += 'a';
        }
        //count each time a letter appears in the ciphertext
        for (int i = 0; i < tempCipher.length(); i++) {
            if (tempCipher[i] == 'a')
                letterCount[0]++;
            if (tempCipher[i] == 'b')
                letterCount[1]++;
            if (tempCipher[i] == 'c')
                letterCount[2]++;
            if (tempCipher[i] == 'd')
                letterCount[3]++;
            if (tempCipher[i] == 'e')
                letterCount[4]++;
            if (tempCipher[i] == 'f')
                letterCount[5]++;
            if (tempCipher[i] == 'g')
                letterCount[6]++;
            if (tempCipher[i] == 'h')
                letterCount[7]++;
            if (tempCipher[i] == 'i')
                letterCount[8]++;
            if (tempCipher[i] == 'j')
                letterCount[9]++;
            if (tempCipher[i] == 'k')
                letterCount[10]++;
            if (tempCipher[i] == 'l')
                letterCount[11]++;
            if (tempCipher[i] == 'm')
                letterCount[12]++;
            if (tempCipher[i] == 'n')
                letterCount[13]++;
            if (tempCipher[i] == 'o')
                letterCount[14]++;
            if (tempCipher[i] == 'p')
                letterCount[15]++;
            if (tempCipher[i] == 'q')
                letterCount[16]++;
            if (tempCipher[i] == 'r')
                letterCount[17]++;
            if (tempCipher[i] == 's')
                letterCount[18]++;
            if (tempCipher[i] == 't')
                letterCount[19]++;
            if (tempCipher[i] == 'u')
                letterCount[20]++;
            if (tempCipher[i] == 'v')
                letterCount[21]++;
            if (tempCipher[i] == 'w')
                letterCount[22]++;
            if (tempCipher[i] == 'x')
                letterCount[23]++;
            if (tempCipher[i] == 'y')
                letterCount[24]++;
            if (tempCipher[i] == 'z')
                letterCount[25]++;
        }
        //Find the new frequencies and sum the deviation from expected values
        for (int i = 0; i < ALPHABET; i++) {
            frequencies[i] = (double)letterCount[i] / cipherText.length();
            deviation += abs(frequencies[i] - EXPECTEDFREQUENCIES[i]);
        }
        if (j == 0) {
            currentDeviation = deviation;
            leastDeviant = keys[j];
        }
        if (deviation < currentDeviation) {
            currentDeviation = deviation;
            leastDeviant = keys[j];
        }
    }
    return leastDeviant;
}

string findKey(string cipherText, int keyLength) {
    vector<string> parsedCipherText(keyLength);
    string key = "";
    if (keyLength > 0) {
        for (int i = 0; i < cipherText.length(); i++) {
            parsedCipherText[i % keyLength] += cipherText[i];
        }
        for (int i = 0; i < keyLength; i++) {
            //For each section of the cipher text, determine the shift using frequency analysis.
            key += frequencyAnalysis(parsedCipherText[i]);
        }
    }
    return key;
}

//Given a key and ciphertext, decrypt() works out what the plaintext is by subtracting the appropriate values from characters
string decrypt(string cipherText, string key) {
    string plainText = cipherText;
    int keyLength = key.length();
    for (int i = 0; i < plainText.length(); i++) {
        plainText[i] -= key[i % keyLength];
        plainText[i] = plainText[i] % ALPHABET; 
        if (plainText[i] < 0) {
            plainText[i] += ALPHABET;
        }
        plainText[i] += 'a';
    }
    return plainText;
}

int main()
{
    string cipherText = getCipherText();
    int keyLength = determineKeyLength(cipherText);
    cout << "The probable key length is " << keyLength << endl;
    string key = findKey(cipherText, keyLength);
    cout << "The likely key is: " << key << endl;
    string plainText = decrypt(cipherText, key);
    cout << "The plaintext is: " << plainText << endl;
}
