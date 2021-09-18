import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { StudentRegisterService } from '../service/student-register.service';

import { StudentRegisterComponent } from './student-register.component';

describe('Component Tests', () => {
  describe('StudentRegister Management Component', () => {
    let comp: StudentRegisterComponent;
    let fixture: ComponentFixture<StudentRegisterComponent>;
    let service: StudentRegisterService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [StudentRegisterComponent],
      })
        .overrideTemplate(StudentRegisterComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StudentRegisterComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(StudentRegisterService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.studentRegisters?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
