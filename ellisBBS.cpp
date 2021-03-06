//The purpose of ellisBBS.cpp is to implement Blum-Blum-Shub to generate a psuedo random sequence of bits
//After this it will calculate the frequencies of zeroes and ones in subsequences of 1000
//Finally, it will find the frequency at which subsequences of length 4 occur.
//Programmed by: Benjamin Ellis
//Date Completed: 03/25/2020
#include<iostream>
#include<bitset>
#include<math.h>

using namespace std;

const long long PRESETP = 1000003;
const long long PRESETQ = 2001911;
const int SEQUENCELENGTH = 100000;

//getAverageZero finds the average number of zeroes in each 1000 length subsequence.
double getAverageZero(bitset<SEQUENCELENGTH> sequence) {
	int zeroes = 0;
	double zeroAvg = 0;
	for (int i = 0; i < sequence.size()-1000; i++) {
		zeroes = 0;
		for (int j = i; j < i + 1000; j++) {
			if (!sequence[j]) {
				zeroes++;
			}
		}
		zeroAvg += (double)zeroes;
	}
	zeroAvg = zeroAvg / (double)(SEQUENCELENGTH - 1000);
	return zeroAvg;
}

//subsequenceFrequency finds how often a given subsequence of length 4 occurs in the sequence
double subsequenceFrequency(bitset<SEQUENCELENGTH> sequence, int subsequence) {
	double frequency = 0;
	for (int i = 0; i < sequence.size() - 4; i++) {
		//Here we check if the section of sequence matches the subsequence of 4 bits.
		if (sequence[i] == (subsequence & 0x8)/8 && sequence[i + 1] == (subsequence & 0x4)/4 && sequence[i + 2] == (subsequence & 0x2)/2 && sequence[i + 3] == (subsequence & 0x1)) {
			frequency++;
		}
	}
	frequency = frequency / (sequence.size()-4);
	return frequency;
}

int main() {
	long long p = PRESETP;
	long long q = PRESETQ;
	long long x = 3;
	long long n = p * q;
	//I am not familiar with bitsets, however ,after a bit of searching, it seemed like the best way to store our sequence
	bitset<SEQUENCELENGTH> sequence;
	for (int i = 0; i < SEQUENCELENGTH; i++) {
		x = (x*x) % n;
		sequence[i] = (x & 0x2);
	}
	cout << "Sequence generated for seed x = 3: " << endl;
	//This loop will print the sequence generated by the program if uncommented
	//I left it commented because having 100,000 bits printed out really serves no purpose other than making a mess
	//for (int i = 0; i < sequence.size(); i++) {
	//	cout << sequence[i];
	//}
	double averageZero = getAverageZero(sequence);
	double averageOne = 1000 - averageZero;
	cout << "The average number of 0's in subsequences of length 1000: " << averageZero << endl;
	cout << "The average number of 1's in subsequences of length 1000: " << averageOne << endl;
	for (int i = 0; i < 16; i++) {
		cout << "The frequency of " << (i & 0x8)/8 << (i & 0x4)/4 << (i & 0x2)/2 << (i & 0x1) << " is: " << subsequenceFrequency(sequence, i) << endl;
	}
}