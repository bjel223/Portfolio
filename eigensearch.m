function [posValues] = eigensearch(A)
   #posEigs-zeroEigs to keep track of eigen value counts
   posEigs = 0;
   numVals = 0;
   tempEig = -1;
   maxValue = norm(A,inf);
   [L,D] = choleskyD(diag(A),diag(A,1));
   for i = 1:length(D)
     if D(i) > 0
       posEigs++;
     else
       zeroEigs++;
     endif
   endfor
   posValues = zeros(posEigs,1);
   [tempEig,numVals] = posSearch(D,L,0,maxValue);
   for i = 1:numVals
     posValues(i) = tempEig;
   endfor
   searchStart = 1+numVals;
   for i = searchStart:posEigs
     [tempEig,numVals] = posSearch(D,L,posValues(i-1),maxValue);
     for j = 1:numVals
       posValues(searchStart) = tempEig;
       searchStart++;
     endfor
     if posEigs < searchStart
       break
     endif
   endfor
endfunction
function [L,D] = choleskyD(A,B)
  n = length(A);
  L = zeros(1,n-1);
  D = zeros(1,n);
  D(1) = A(1);
  for j = 1:n-1
    L(j) = B(j)/D(j);
    D(j+1) = A(j+1)-D(j)*L(j)^2;
  endfor
endfunction
function [M,E,r] = choleskyE(D,L,rho)
  qualityControl = 1;
  epsilon = 10^-15;
  r = rho;
  while qualityControl != 0
   qualityControl = 0;
   n = length(D);
   M = zeros(1,n-1);
   E = zeros(1,n);
   E(1) = D(1)-r;
   for j = 1:n-1
     if E(j)!=0
       M(j) = L(j)*D(j)/E(j);
     endif
     if (E(j)*M(j) - D(j)*L(j))>epsilon
       #E,M,D,L
       qualityControl = 1;
       r += epsilon;
     endif
     E(j+1) = D(j+1)+D(j)*L(j)^2-E(j)*M(j)^2-r;
   endfor
  endwhile
endfunction
function [b,numVals] = posSearch(D,L,a,b)
   epsilon = 10^-15;
   #ERROR is predefined accuracy of found eigen value
   ERROR = 2^(-100);
   #posEigs-zeroEigs to keep track of eigen value counts
   posEigs = 0;
   negEigs = 0;
   zeroEigs = 0;
   #eValue is the eigen value the program is zeroing in on
   eValue = 0;
   depth = 0;
   [M,E,a] = choleskyE(D,L,a);
   for i = 1:length(E)
     if E(i) > 0
       posEigs++;
     endif
   endfor
   rho = (b-a)/2;
   while(2^(-depth)>ERROR)
    eValue = (b+a)/2;
    newPosEigs = 0;
    depth++;
    [M,E,eValue] = choleskyE(D,L,eValue);
    for i = 1:length(E)
     if E(i) > 0
       newPosEigs++;
     endif 
    endfor
    if newPosEigs >= posEigs
      a = (a+b)/2;
      rho = (b-a)/2;
    elseif newPosEigs < posEigs
      b = (a+b)/2;
      rho = (b-a)/2;
    endif
   endwhile
   [M,E,b] = choleskyE(D,L,b);
   newPosEigs = 0;
   for i = 1:length(E)
     if E(i) > 0
       newPosEigs++;
     endif
   endfor
   numVals = abs(posEigs - newPosEigs);
endfunction
