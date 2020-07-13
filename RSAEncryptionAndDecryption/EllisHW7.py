# The purpose of this program is to act as an implementation of RSA.
# As such, it will generate two large primes p and q, then the public keys n and e, and the private key d.
# From here users can input plaintext to encrypt with the created RSA system.
# Created by Benjamin Ellis for the purpose of CS378 HW7.
# Due Date: 4/23/20

import random

# Global Constants used to limit rand functions, these will limit the size of p,q and e,d respectively.
MAXBITS = 600
MAXKEY = 600


# Modular exponentiation method to handle all modular exponentiation required for RSA
def modEx(a, x, n):
    i = 0
    total = 1
    b = a
    while x > (2**i)-1:
        if x & (2**i):
            total = total*b % n
        b = b**2 % n
        i += 1
    return total


# Extended euclids algorithm to find both the gcd and inverse of e.
def gcdExtended(a, b):
    if a != 0:
        gcd, x, y = gcdExtended(b % a, a)
        y = y - (b // a) * x
        return gcd, y, x
    else:
        return b, 0, 1


# This function is based on the pseudo code featured in the zoom notes on canvas
# However, it does not seem to work correctly. As a result, I have opted to use the less effective
# Fermat test, unless I have extra time to figure out what's going wrong here
def millerRabin(a, n):
    m = n - 1
    k = 0
    while m % 2 == 0:
        m = m/2
        k += 1
    b = modEx(a, m, n)
    if b == 1 or b == -1:
        return 1
    else:
        for i in range(1, k - 1):
            b = modEx(b, 2, n)
            if b == 1:
                return 0
            elif b == -1:
                return 1
        return 0


# Implementation of using FLT to test the primality of a number
def fermatTest(a, p):
    if modEx(a, p - 1, p) == 1:
        return 1
    else:
        return 0


def checkPrime(p):
    # This if is necessary because of how the range is set up in the random function
    if p == 2:
        return 1
    elif p == 1:
        return 0
    # We repeat fermat's test 10 times with random values between 2 and p-1 to decrease the chances of an
    # n pseudo prime slipping through
    for i in range(0, 9):
        if fermatTest(random.randint(2, p - 1), p) != 1:
            return 0
    return 1


def genPrime():
    k = random.randint(MAXBITS // 2, MAXBITS)
    prime = 2
    while prime % 2 == 0:
        prime = random.getrandbits(k)
    if checkPrime(prime):
        return prime
    else:
        return genPrime()


#Function to generate our public and private keys e and d
def genE(p, q):
    n = (p - 1) * (q - 1)
    # k's size is determined by the MAXKEY value
    k = random.getrandbits(MAXKEY)
    e = 2
    # An even e can't be coprime with (p-1)(q-1), so reroll it immediately
    while e % 2 == 0:
        e = random.randint(k // 2, k)
    gcd, d, y = gcdExtended(e, n)
    if d < 0:
        d += n
    # If is appropriately coprime, we have our keys. Otherwise try again.
    if gcd == 1:
        return e, d
    else:
        return genE(p, q)


# Decrypt gets the public keys and private keys from their files and the ciphertext from its file
# then calls modEx to decrypt the message
def decrypt():
    f = open('public_key', 'r')
    n = f.readline()
    n = int(n)
    f.close()
    f = open('private_key', 'r')
    d = f.readline()
    d = int(d)
    f.close()
    f = open('ciphertext', 'r')
    ct = f.readline()
    ct = int(ct)
    f.close()
    plaintext = modEx(ct, d, n)
    f = open('decrypted_message', 'w')
    f.write(str(plaintext))
    f.close()


# Encrypt reads public key information from the public_key file and message information from message
# It then uses modEx to encrypt the message. From there it writes the ciphertext to the ciphertext file
def encrypt():
    f = open('public_key', 'r')
    n = f.readline()
    n = int(n)
    e = f.readline()
    e = int(e)
    f.close()
    f = open('message', 'r')
    message = f.readline()
    message = int(message)
    f.close()
    ciphertext = modEx(message, e, n)
    f = open('ciphertext', 'w')
    f.write(str(ciphertext))
    f.close()


# keySetup generates two large primes p and q, and calls genE to generate e and d
# The public keys are written to the public_key file by having n written then a new line then e.
def keySetup():
    p = genPrime()
    q = genPrime()
    e, d = genE(p, q)
    n = p*q
    # Write the keys to their appropriate files, with the public keys separated by a new line
    f = open('public_key', 'w')
    f.write(str(n))
    f.write('\n')
    f.write(str(e))
    f.close()
    f = open('private_key', 'w')
    f.write(str(d))
    f.close()


def main():
    keySetup()
    encrypt()
    decrypt()


main()
