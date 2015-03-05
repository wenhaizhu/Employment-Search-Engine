'''
Clarification:the implementation of simhash algorithm is forked from github https://github.com/sangelone/python-hashes
implemented by third party
'''
from hashtype import hashtype

class simhash(hashtype):
    def create_hash(self, tokens):

        if type(tokens) == str:
            tokens = tokens.split()
        v = [0]*self.hashbits    
        for t in [self._string_hash(x) for x in tokens]:
            bitmask = 0
            for i in xrange(self.hashbits):
                bitmask = 1 << i
                if t & bitmask:
                    v[i] += 1
                else:
                    v[i] -= 1

        fingerprint = 0
        for i in xrange(self.hashbits):
            if v[i] >= 0:
                fingerprint += 1 << i        
        self.hash = fingerprint

    def _string_hash(self, v):
       
        if v == "":
            return 0
        else:
            x = ord(v[0])<<7
            m = 1000003
            mask = 2**self.hashbits-1
            for c in v:
                x = ((x*m)^ord(c)) & mask
            x ^= len(v)
            if x == -1: 
                x = -2
            return x

    def similarity(self, other_hash):
        """Calculate how similar this hash is from another simhash.
        Returns a float from 0.0 to 1.0 (linear distribution, inclusive)
        """
        if type(other_hash) != simhash:
            raise Exception('Hashes must be of same type to find similarity')
        b = self.hashbits
        if b!= other_hash.hashbits:
            raise Exception('Hashes must be of equal size to find similarity')
        return float(b - self.hamming_distance(other_hash)) / b