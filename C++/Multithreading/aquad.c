//Worker/ Farmer multithreading model using MPI

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <mpi.h>
#include "stack.h"

#define EPSILON 1e-3
#define F(arg)  cosh(arg)*cosh(arg)*cosh(arg)*cosh(arg)
#define A 0.0
#define B 5.0

#define SLEEPTIME 1

int *tasks_per_process;

double farmer(int);

void worker(int);

int main(int argc, char **argv ) {
  int i, myid, numprocs;
  double area, a, b;

  MPI_Init(&argc, &argv);
  MPI_Comm_size(MPI_COMM_WORLD,&numprocs);
  MPI_Comm_rank(MPI_COMM_WORLD,&myid);


  if(numprocs < 2) {
    fprintf(stderr, "ERROR: Must have at least 2 processes to run\n");
    MPI_Finalize();
    exit(1);
  }

  if (myid == 0) { // Farmer
    // init counters
    tasks_per_process = (int *) malloc(sizeof(int)*(numprocs));
    for (i=0; i<numprocs; i++) {
      tasks_per_process[i]=0;
    }
  }

  if (myid == 0) { // Farmer
    area = farmer(numprocs);
  } else { //Workers
    worker(myid);
  }

  if(myid == 0) {
    fprintf(stdout, "Area=%lf\n", area);
    fprintf(stdout, "\nTasks Per Process\n");
    for (i=0; i<numprocs; i++) {
      fprintf(stdout, "%d\t", i);
    }
    fprintf(stdout, "\n");
    for (i=0; i<numprocs; i++) {
      fprintf(stdout, "%d\t", tasks_per_process[i]);
    }
    fprintf(stdout, "\n");
    free(tasks_per_process);
  }
  MPI_Finalize();
  return 0;
}

double farmer(int numprocs) {

	int activeCheck[numprocs], tag, who;
	activeCheck[0] = 1;
	stack *stack = new_stack();
	double points[2], *data, area;
	area = 0.0;

	points[0] = A;
	points[1] = B;
	push(points, stack);
	int complete = 0;

	while(complete == 0){

		int j;

		MPI_Status status;

		for(j = 1; j < numprocs; j++){
			if(activeCheck[j] == 0 && !is_empty(stack)){
				data = pop(stack);

				MPI_Send(data, 2, MPI_DOUBLE, j, 0, MPI_COMM_WORLD);

				activeCheck[j] = 1;
			}
		}

		int checkForActive = 0;
		for(j=1; j < numprocs; j++){
			if (activeCheck[j] == 1){
				checkForActive = 1;
			}
		}

		if(is_empty(stack) && checkForActive == 0){
			for(j = 1; j < numprocs; j++){
				MPI_Send(data, 2, MPI_DOUBLE, j, 1, MPI_COMM_WORLD);
			}
			complete = 1;
		}

		if(checkForActive == 1){
			double returnedData[3], newTask1[2], newTask2[2];
			MPI_Recv(returnedData, 3, MPI_DOUBLE, MPI_ANY_SOURCE, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
			who = status.MPI_SOURCE;
			tag = status.MPI_TAG;

			if(tag == 1){
				area = area + returnedData[0];
				activeCheck[who] = 0;
				tasks_per_process[who] += 1;
			} else {
				newTask1[0] = returnedData[0]; newTask1[1] = returnedData[1];
				push(newTask1, stack);
				newTask2[0] = returnedData[1]; newTask2[1] = returnedData[2];
				push(newTask2, stack);
				activeCheck[who] = 0;
				tasks_per_process[who] += 1;
			}
		}
	}
	return area;
}

void worker(int mypid) {

	int complete = 0;
	int tag;
	MPI_Status status;

	while(complete == 0){
		double receivedData[2];
		MPI_Recv(receivedData, 2, MPI_DOUBLE,0, MPI_ANY_TAG, MPI_COMM_WORLD, &status);
		tag = status.MPI_TAG;
		usleep(SLEEPTIME);
		if(tag == 1){
			complete = 1;
		} else if(tag == 0) {

			double left = receivedData[0];
			double right = receivedData[1];
			double fleft = F(receivedData[0]);
			double fright = F(receivedData[1]);
			double lrarea = ((fleft+fright) * (right-left)/2);

			double mid, fmid, larea, rarea;

			mid = (left + right) / 2;
			fmid = F(mid);
			larea = (fleft + fmid) * (mid - left) / 2;
			rarea = (fmid + fright) * (right - mid) / 2;

			if( fabs((larea + rarea) - lrarea) > EPSILON ) {
				double sendData[3];
				sendData[0] = left;
				sendData[1] = mid;
				sendData[2] = right;
				MPI_Ssend(sendData, 3, MPI_DOUBLE, 0, 0, MPI_COMM_WORLD);

			} else {
				double sendData[3];
				sendData[0] = larea + rarea;
				MPI_Ssend(sendData, 3, MPI_DOUBLE, 0, 1, MPI_COMM_WORLD);
			}
		}
	}
}
