import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StudentRegisterDetailComponent } from './student-register-detail.component';

describe('Component Tests', () => {
  describe('StudentRegister Management Detail Component', () => {
    let comp: StudentRegisterDetailComponent;
    let fixture: ComponentFixture<StudentRegisterDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [StudentRegisterDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ studentRegister: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(StudentRegisterDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StudentRegisterDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load studentRegister on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.studentRegister).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
