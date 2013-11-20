#include <stdio.h>
#include <stdlib.h>

#include <GL/glut.h>
#define GLUT_KEY_ESCAPE 27
#ifndef GLUT_WHEEL_UP
    #define GLUT_WHEEL_UP 3
    #define GLUT_WHEEL_DOWN 4
#endif

#include "Vector.h"
#include "Matrix.h"
#include "Volume.h"

#define WIDTH 756
#define HEIGHT 256

int lowerRed = 75;
int upperRed = 85;

int lowerBlue = 100;
int upperBlue = 170;

static Volume* head = NULL;

void Update() {
	glutPostRedisplay();
}

void Draw() {

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glBegin(GL_POINTS);

	for(int y = 0; y < head->GetHeight()-50; y++)
		for(int z = 0; z < head->GetDepth(); z++) {
			float distance = 0.0;
			float alpha = 0.0;
			float intensity = 0.0;
			float red = 0.0;
			float blue = 0.0;
			float check = 0.0;

			for (int x = 0; x < head->GetWidth() - 20; x++) {
				unsigned char val = head->Get(x, y, z);

				if (alpha < 1){
					if((val > lowerRed) && (val < upperRed)){

						alpha = ((float)val/255.0) + ((1.0 - ((float)val/255.0)) * alpha);
						red = red + 1.0;
						intensity = intensity + (1.0 - alpha) * (((float)val/255.0));
					}else if((val > lowerBlue) && (val < upperBlue)){

						alpha = ((float)val/255.0) + ((1.0 - ((float)val/255.0)) * alpha);
						blue = blue + 1.0;
						intensity = intensity + (1.0 - alpha) * (((float)val/255.0));
					}

				} else if((alpha >= 1) && (check == 0)){
					distance = x;
					check = 1;

				}

			}

		float total = red + blue;

		if(total != 0.0){
			Vector3 color = Vector3((red/total), intensity - (distance/255.0), (blue/total));
			glColor3f(color.r(), color.g(), color.b());
			glVertex3f(y, z*2, 0);
		}

	}

	for(int x = 0; x < head->GetWidth() - 20; x++)
		for(int z = 0; z < head->GetDepth(); z++) {
			float distance = 0.0;
			float alpha = 0.0;
			float intensity = 0.0;
			float red = 0.0;
			float blue = 0.0;
			float check = 0.0;

			for (int y = 0; y < head->GetHeight() - 40; y++) {
				unsigned char val = head->Get(x, y, z);

				if (alpha < 1){
					if((val > lowerRed) && (val < upperRed)){

						alpha = ((float)val/255.0) + ((1.0 - ((float)val/255.0)) * alpha);
						red = red + 1.0;
						intensity = intensity + (1.0 - alpha) * (((float)val/255.0));
					}else if((val > lowerBlue) && (val < upperBlue)){

						alpha = ((float)val/255.0) + ((1.0 - ((float)val/255.0)) * alpha);
						blue = blue + 1.0;
						intensity = intensity + (1.0 - alpha) * (((float)val/255.0));
					}

				} else if((alpha >= 1) && (check == 0)){
					distance = y;
					check = 1;

				}

			}

		float total = red + blue;

		if(total != 0.0){
			Vector3 color = Vector3((red/total), intensity - (distance/255.0), (blue/total));
			glColor3f(color.r(), color.g(), color.b());
			glVertex3f(x + 256, z*2, 0);
		}

	}

	for(int x = 0; x < head->GetWidth() - 20; x++)
		for(int y = 0; y < head->GetHeight(); y++) {
			float distance = 0.0;
			float alpha = 0.0;
			float intensity = 0.0;
			float red = 0.0;
			float blue = 0.0;
			float check = 0.0;

			for (int z = 0; z < head->GetDepth() - 40; z++) {
				unsigned char val = head->Get(x, y, z);

				if (alpha < 1){
					if((val > lowerRed) && (val < upperRed)){

						alpha = ((float)val/255.0) + ((1.0 - ((float)val/255.0)) * alpha);
						red = red + 1.0;
						intensity = intensity + (1.0 - alpha) * (((float)val/255.0));
					}else if((val > lowerBlue) && (val < upperBlue)){

						alpha = ((float)val/255.0) + ((1.0 - ((float)val/255.0)) * alpha);
						blue = blue + 1.0;
						intensity = intensity + (1.0 - alpha) * (((float)val/255.0));
					}

				} else if((alpha >= 1) && (check == 0)){
					distance = z;
					check = 1;

				}

			}

		float total = red + blue;

		if(total != 0.0){
			Vector3 color = Vector3((red/total), intensity - (distance/255.0), (blue/total));
			glColor3f(color.r(), color.g(), color.b());
			glVertex3f(y + 512, x, 0);
		}

	}


	glEnd();
	glFlush();
	glutSwapBuffers();
}

void KeyEvent(unsigned char key, int x, int y) {

	switch(key) {
    case GLUT_KEY_ESCAPE:
      exit(EXIT_SUCCESS);
      break;
    case 'q':
    	lowerRed -= 1;
    case 'w':
    	lowerRed += 1;
    case 'a':
    	upperRed -= 1;
    case 's':
    	upperRed += 1;
    case 'o':
    	lowerBlue -= 1;
    case 'p':
    	lowerBlue += 1;
    case 'k':
    	upperBlue -= 1;
    case 'l':
    	upperBlue += 1;
	}
	
}



int main(int argc, char **argv) {

	head = new Volume("head");

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB|GLUT_DOUBLE|GLUT_DEPTH|GLUT_MULTISAMPLE);
	glutInitWindowSize(WIDTH, HEIGHT);
	glutCreateWindow("cav2");

	glClearColor(0.0, 0.0, 0.0, 1.0);

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(0, WIDTH, HEIGHT, 0, -512, 512);

	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

	glDisable(GL_DEPTH_TEST);

	glutKeyboardFunc(KeyEvent);
	glutDisplayFunc(Draw);
	glutIdleFunc(Update);

	glutMainLoop();

	delete head;
};
