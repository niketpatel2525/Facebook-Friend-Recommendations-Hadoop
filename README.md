# Facebook Friend Recommendations Hadoop


This repository contains hadoop code for suggesting facebook friends to the user. There are two version of hadoop code depending upon input. 

Verson-1 contains simple input format such as 
```
(id [list_of_friends_id]) 
```
Version-2 contains JSON format input such as 
```
({id:xyz, hometown:'Chicago,IL',name='Joe'}, [{id:abc, hometown:'Chicago,IL',name='Bob'},{id:byx, hometown:'Chicago,IL',name='Anna'},{id:evcr, hometown:'Chicago,IL',name='Tom'}]) 
```


### Approach v1

Let's take input
```
0    1,2,3
1    0,2,3,4,5
2    0,1,4
3    0,1,4
4    1,2,3
5    1,6
6    5
```
Here, user 0 is not friend of user 4 and 5, but user 0 and user 4 have mutual friend [1,2,3] and user 0 and user 5 have mutual friend [1]. So we would like to recommend user 4 and 5 as friends of user 0.


The output recommended friends will be given in the following format.
<Recommended Friend to USER(# of mutual friends: [the id of mutual friend ,...],...)>. The output result will be sorted according to the number of mutual friends and can be verfied from the graph. 

```
0    4 (3: [3, 1, 2]),5 (1: [1])
1    6 (1: [5])
2    3 (3: [1, 4, 0]),5 (1: [1])
3    2 (3: [4, 0, 1]),5 (1: [1])
4    0 (3: [2, 3, 1]),5 (1: [1])
5    0 (1: [1]),2 (1: [1]),3 (1: [1]),4 (1: [1])
6    1 (1: [5])
```

### Approach v2


