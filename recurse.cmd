@echo off
rem Usage:  recurse <command>
rem 
rem Walk the submodules recursively and execute the command at
rem every level.  By default, it steps into git submodules, but
rem if there is a recurse script in a subdirectory, it will call
rem that one instead.  

rem This file is licensed under the terms of the Apache 2.0 License.
rem   the text is included at :ApacheLicense at the bottom of the file.

setlocal
set command=%*
rem Get the subdirectories from .gitmodules.  If it doesn't
rem exist, don't process any subdirectories
if exist .gitmodules (
    for /f %%f in ('awk "/path += +/{print $3}" ^< .gitmodules') do (
        if not "%%f" == "echo" (
            call :processDir %%f
        )
    )
)
rem After the subdirectories are done, process the current directory.
for %%f in (.) do (
   echo # %%~ff
   %command%
 )
goto :eof

:processDir
    rem Replace slashes with backslashes in the directory name
    for /f %%d in ('echo %1 ^| sed s/\//\\/g;') do (
        set dirName=%%d
    )
    if exist %dirName% (
        pushd .\%dirName%
        rem Now that we are in the subdirectory, call
        rem    recurse again.  We use cmd /c because it has
        rem    to be in a separate process.  Also, if there
        rem    is a recurse script in the subdirectory, it
        rem    will use that one instead.
        cmd /c recurse %command%
        popd
    )
    goto :eof
endlocal

:ApacheLicense
rem  
rem                                   Apache License
rem                             Version 2.0, January 2004
rem                          http://www.apache.org/licenses/
rem  
rem     TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION
rem  
rem     1. Definitions.
rem  
rem        "License" shall mean the terms and conditions for use, reproduction,
rem        and distribution as defined by Sections 1 through 9 of this document.
rem  
rem        "Licensor" shall mean the copyright owner or entity authorized by
rem        the copyright owner that is granting the License.
rem  
rem        "Legal Entity" shall mean the union of the acting entity and all
rem        other entities that control, are controlled by, or are under common
rem        control with that entity. For the purposes of this definition,
rem        "control" means (i) the power, direct or indirect, to cause the
rem        direction or management of such entity, whether by contract or
rem        otherwise, or (ii) ownership of fifty percent (50%) or more of the
rem        outstanding shares, or (iii) beneficial ownership of such entity.
rem  
rem        "You" (or "Your") shall mean an individual or Legal Entity
rem        exercising permissions granted by this License.
rem  
rem        "Source" form shall mean the preferred form for making modifications,
rem        including but not limited to software source code, documentation
rem        source, and configuration files.
rem  
rem        "Object" form shall mean any form resulting from mechanical
rem        transformation or translation of a Source form, including but
rem        not limited to compiled object code, generated documentation,
rem        and conversions to other media types.
rem  
rem        "Work" shall mean the work of authorship, whether in Source or
rem        Object form, made available under the License, as indicated by a
rem        copyright notice that is included in or attached to the work
rem        (an example is provided in the Appendix below).
rem  
rem        "Derivative Works" shall mean any work, whether in Source or Object
rem        form, that is based on (or derived from) the Work and for which the
rem        editorial revisions, annotations, elaborations, or other modifications
rem        represent, as a whole, an original work of authorship. For the purposes
rem        of this License, Derivative Works shall not include works that remain
rem        separable from, or merely link (or bind by name) to the interfaces of,
rem        the Work and Derivative Works thereof.
rem  
rem        "Contribution" shall mean any work of authorship, including
rem        the original version of the Work and any modifications or additions
rem        to that Work or Derivative Works thereof, that is intentionally
rem        submitted to Licensor for inclusion in the Work by the copyright owner
rem        or by an individual or Legal Entity authorized to submit on behalf of
rem        the copyright owner. For the purposes of this definition, "submitted"
rem        means any form of electronic, verbal, or written communication sent
rem        to the Licensor or its representatives, including but not limited to
rem        communication on electronic mailing lists, source code control systems,
rem        and issue tracking systems that are managed by, or on behalf of, the
rem        Licensor for the purpose of discussing and improving the Work, but
rem        excluding communication that is conspicuously marked or otherwise
rem        designated in writing by the copyright owner as "Not a Contribution."
rem  
rem        "Contributor" shall mean Licensor and any individual or Legal Entity
rem        on behalf of whom a Contribution has been received by Licensor and
rem        subsequently incorporated within the Work.
rem  
rem     2. Grant of Copyright License. Subject to the terms and conditions of
rem        this License, each Contributor hereby grants to You a perpetual,
rem        worldwide, non-exclusive, no-charge, royalty-free, irrevocable
rem        copyright license to reproduce, prepare Derivative Works of,
rem        publicly display, publicly perform, sublicense, and distribute the
rem        Work and such Derivative Works in Source or Object form.
rem  
rem     3. Grant of Patent License. Subject to the terms and conditions of
rem        this License, each Contributor hereby grants to You a perpetual,
rem        worldwide, non-exclusive, no-charge, royalty-free, irrevocable
rem        (except as stated in this section) patent license to make, have made,
rem        use, offer to sell, sell, import, and otherwise transfer the Work,
rem        where such license applies only to those patent claims licensable
rem        by such Contributor that are necessarily infringed by their
rem        Contribution(s) alone or by combination of their Contribution(s)
rem        with the Work to which such Contribution(s) was submitted. If You
rem        institute patent litigation against any entity (including a
rem        cross-claim or counterclaim in a lawsuit) alleging that the Work
rem        or a Contribution incorporated within the Work constitutes direct
rem        or contributory patent infringement, then any patent licenses
rem        granted to You under this License for that Work shall terminate
rem        as of the date such litigation is filed.
rem  
rem     4. Redistribution. You may reproduce and distribute copies of the
rem        Work or Derivative Works thereof in any medium, with or without
rem        modifications, and in Source or Object form, provided that You
rem        meet the following conditions:
rem  
rem        (a) You must give any other recipients of the Work or
rem            Derivative Works a copy of this License; and
rem  
rem        (b) You must cause any modified files to carry prominent notices
rem            stating that You changed the files; and
rem  
rem        (c) You must retain, in the Source form of any Derivative Works
rem            that You distribute, all copyright, patent, trademark, and
rem            attribution notices from the Source form of the Work,
rem            excluding those notices that do not pertain to any part of
rem            the Derivative Works; and
rem  
rem        (d) If the Work includes a "NOTICE" text file as part of its
rem            distribution, then any Derivative Works that You distribute must
rem            include a readable copy of the attribution notices contained
rem            within such NOTICE file, excluding those notices that do not
rem            pertain to any part of the Derivative Works, in at least one
rem            of the following places: within a NOTICE text file distributed
rem            as part of the Derivative Works; within the Source form or
rem            documentation, if provided along with the Derivative Works; or,
rem            within a display generated by the Derivative Works, if and
rem            wherever such third-party notices normally appear. The contents
rem            of the NOTICE file are for informational purposes only and
rem            do not modify the License. You may add Your own attribution
rem            notices within Derivative Works that You distribute, alongside
rem            or as an addendum to the NOTICE text from the Work, provided
rem            that such additional attribution notices cannot be construed
rem            as modifying the License.
rem  
rem        You may add Your own copyright statement to Your modifications and
rem        may provide additional or different license terms and conditions
rem        for use, reproduction, or distribution of Your modifications, or
rem        for any such Derivative Works as a whole, provided Your use,
rem        reproduction, and distribution of the Work otherwise complies with
rem        the conditions stated in this License.
rem  
rem     5. Submission of Contributions. Unless You explicitly state otherwise,
rem        any Contribution intentionally submitted for inclusion in the Work
rem        by You to the Licensor shall be under the terms and conditions of
rem        this License, without any additional terms or conditions.
rem        Notwithstanding the above, nothing herein shall supersede or modify
rem        the terms of any separate license agreement you may have executed
rem        with Licensor regarding such Contributions.
rem  
rem     6. Trademarks. This License does not grant permission to use the trade
rem        names, trademarks, service marks, or product names of the Licensor,
rem        except as required for reasonable and customary use in describing the
rem        origin of the Work and reproducing the content of the NOTICE file.
rem  
rem     7. Disclaimer of Warranty. Unless required by applicable law or
rem        agreed to in writing, Licensor provides the Work (and each
rem        Contributor provides its Contributions) on an "AS IS" BASIS,
rem        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
rem        implied, including, without limitation, any warranties or conditions
rem        of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A
rem        PARTICULAR PURPOSE. You are solely responsible for determining the
rem        appropriateness of using or redistributing the Work and assume any
rem        risks associated with Your exercise of permissions under this License.
rem  
rem     8. Limitation of Liability. In no event and under no legal theory,
rem        whether in tort (including negligence), contract, or otherwise,
rem        unless required by applicable law (such as deliberate and grossly
rem        negligent acts) or agreed to in writing, shall any Contributor be
rem        liable to You for damages, including any direct, indirect, special,
rem        incidental, or consequential damages of any character arising as a
rem        result of this License or out of the use or inability to use the
rem        Work (including but not limited to damages for loss of goodwill,
rem        work stoppage, computer failure or malfunction, or any and all
rem        other commercial damages or losses), even if such Contributor
rem        has been advised of the possibility of such damages.
rem  
rem     9. Accepting Warranty or Additional Liability. While redistributing
rem        the Work or Derivative Works thereof, You may choose to offer,
rem        and charge a fee for, acceptance of support, warranty, indemnity,
rem        or other liability obligations and/or rights consistent with this
rem        License. However, in accepting such obligations, You may act only
rem        on Your own behalf and on Your sole responsibility, not on behalf
rem        of any other Contributor, and only if You agree to indemnify,
rem        defend, and hold each Contributor harmless for any liability
rem        incurred by, or claims asserted against, such Contributor by reason
rem        of your accepting any such warranty or additional liability.
rem  
rem     END OF TERMS AND CONDITIONS
rem  
rem     APPENDIX: How to apply the Apache License to your work.
rem  
rem        To apply the Apache License to your work, attach the following
rem        boilerplate notice, with the fields enclosed by brackets "[]"
rem        replaced with your own identifying information. (Don't include
rem        the brackets!)  The text should be enclosed in the appropriate
rem        comment syntax for the file format. We also recommend that a
rem        file or class name and description of purpose be included on the
rem        same "printed page" as the copyright notice for easier
rem        identification within third-party archives.
rem  
rem     Copyright 2023 Edward Cole
rem  
rem     Licensed under the Apache License, Version 2.0 (the "License");
rem     you may not use this file except in compliance with the License.
rem     You may obtain a copy of the License at
rem  
rem         http://www.apache.org/licenses/LICENSE-2.0
rem  
rem     Unless required by applicable law or agreed to in writing, software
rem     distributed under the License is distributed on an "AS IS" BASIS,
rem     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem     See the License for the specific language governing permissions and
rem     limitations under the License.