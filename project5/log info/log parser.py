import os
def reader():

    path = os.getcwd()
    
    f = open(path+"/TimeLogs", "r")
    text = f.readlines()
    sum1=0
    sum2=0
    inside=[]
    for i in text:
        inside.append(i.strip())
   
    for i in inside:
        l= i.split("-")
        sum1+=int(l[0])
        sum2+=int(l[1])
    sum1=sum1/len(inside)
    sum2=sum2/len(inside)
    
    print("TS: "+str(sum1))
    print("TJ: "+str(sum2))
    
if __name__ == "__main__":
    reader()
 
